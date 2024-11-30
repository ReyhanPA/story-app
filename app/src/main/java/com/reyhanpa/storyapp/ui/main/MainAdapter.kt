package com.reyhanpa.storyapp.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reyhanpa.storyapp.data.remote.response.ListStoryItem
import com.reyhanpa.storyapp.databinding.ItemStoryBinding
import com.reyhanpa.storyapp.ui.detail.DetailStoryActivity
import com.reyhanpa.storyapp.utils.DiffUtilCallback

class MainAdapter : ListAdapter<ListStoryItem, MainAdapter.MainViewHolder>(DiffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

    class MainViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.imgItemStory)
            binding.tvItemStoryName.text = story.name
            binding.tvItemStoryDescription.text = story.description
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY_IMAGE, story.photoUrl)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY_NAME, story.name)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY_DESCRIPTION, story.description)
                binding.root.context.startActivity(intent)
            }
        }
    }
}