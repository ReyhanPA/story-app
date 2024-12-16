package com.reyhanpa.storyapp.ui.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.reyhanpa.storyapp.BuildConfig
import com.reyhanpa.storyapp.R
import com.reyhanpa.storyapp.databinding.ActivityUploadBinding
import com.reyhanpa.storyapp.ui.ViewModelFactory
import com.reyhanpa.storyapp.ui.main.MainActivity
import com.reyhanpa.storyapp.utils.getImageUri
import com.reyhanpa.storyapp.utils.reduceFileImage
import com.reyhanpa.storyapp.utils.uriToFile

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentImageUri: Uri? = null
    private var currentLocation: Location? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.apply {
            buttonGallery.setOnClickListener { startGallery() }
            buttonCamera.setOnClickListener { startCamera() }
            buttonAdd.setOnClickListener { uploadImage() }
            swAddLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    requestLocation()
                } else {
                    currentLocation = null
                }
            }
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri: Uri? ->
        if (imageUri != null) {
            currentImageUri = imageUri
            showImage()
        } else {
            if (BuildConfig.DEBUG) Log.d(TAG, "Error: ${resources.getString(R.string.no_image_selected)}")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.imgPreviewStory.setImageURI(it)
        }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
            } else {
                logAndShowError(R.string.location_not_found)
            }
        }.addOnFailureListener {
            logAndShowError(R.string.location_error)
        }
    }

    private fun uploadImage() {
        val imageUri = currentImageUri
        val description = binding.edAddDescription.text.toString().trim()

        if (imageUri == null) {
            showToast(getString(R.string.empty_image_warning))
            return
        }

        if (description.isEmpty()) {
            showToast(getString(R.string.empty_description_warning))
            return
        }

        val imageFile = uriToFile(imageUri, this).reduceFileImage()
        val latitude = currentLocation?.latitude
        val longitude = currentLocation?.longitude

        viewModel.uploadImage(imageFile, description, latitude, longitude).observe(this) { result ->
            when {
                result == null -> {
                    logAndShowError(R.string.null_error)
                }
                result.error == true -> {
                    val message = result.message ?: getString(R.string.unknown_error)
                    showToast(message)
                }
                result.error == false -> {
                    val message = result.message ?: getString(R.string.success_upload)
                    showToast(message)
                    backToMain()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logAndShowError(messageResId: Int) {
        val message = getString(messageResId)
        showToast(message)
    }

    private fun backToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    companion object {
        private const val TAG = "UploadActivity"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }
}