package com.reyhanpa.storyapp.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reyhanpa.storyapp.R
import com.reyhanpa.storyapp.data.remote.response.ListStoryItem
import com.reyhanpa.storyapp.databinding.ItemStoryBinding
import com.reyhanpa.storyapp.ui.detail.DetailActivity
import com.reyhanpa.storyapp.utils.DiffUtilCallback

class MainAdapter : PagingDataAdapter<ListStoryItem, MainAdapter.MainViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    class MainViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.imgItemStory)
            binding.tvItemStoryName.text = story.name
            binding.tvItemStoryDescription.text = story.description
            if (story.lat != null && story.lon != null) {
                val context = binding.root.context
                val locationText = context.getString(R.string.story_location, story.lat.toString(), story.lon.toString())
                binding.tvItemStoryLocation.text = locationText
                binding.tvItemStoryLocation.visibility = View.VISIBLE
            } else {
                binding.tvItemStoryLocation.visibility = View.GONE
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STORY_IMAGE, story.photoUrl)
                intent.putExtra(DetailActivity.EXTRA_STORY_NAME, story.name)
                intent.putExtra(DetailActivity.EXTRA_STORY_DESCRIPTION, story.description)
                intent.putExtra(DetailActivity.EXTRA_LATITUDE, story.lat)
                intent.putExtra(DetailActivity.EXTRA_LONGITUDE, story.lon)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgItemStory, "image"),
                        Pair(binding.tvItemStoryName, "name"),
                        Pair(binding.tvItemStoryDescription, "description"),
                        Pair(binding.tvItemStoryLocation, "location")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}