package com.reyhanpa.storyapp.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.reyhanpa.storyapp.R
import com.reyhanpa.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val storyImage = intent.getStringExtra(EXTRA_STORY_IMAGE)
        val storyName = intent.getStringExtra(EXTRA_STORY_NAME)
        val storyDescription = intent.getStringExtra(EXTRA_STORY_DESCRIPTION)
        val latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)

        binding.apply {
            Glide.with(binding.root.context)
                .load(storyImage)
                .into(binding.imgItemStory)
            tvItemStoryName.text = storyName
            tvItemStoryDescription.text = storyDescription
            val context = binding.root.context
            val locationText = context.getString(R.string.story_location, latitude.toString(), longitude.toString())
            binding.tvItemStoryLocation.text = locationText
            binding.tvItemStoryLocation.visibility = View.VISIBLE
        }
    }

    companion object {
        const val EXTRA_STORY_IMAGE = "extra_story_image"
        const val EXTRA_STORY_NAME = "extra_story_name"
        const val EXTRA_STORY_DESCRIPTION = "extra_story_description"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
    }
}