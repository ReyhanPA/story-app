package com.reyhanpa.storyapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.reyhanpa.storyapp.data.remote.response.ListStoryItem

object DiffUtilCallback : DiffUtil.ItemCallback<ListStoryItem>() {
    override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
        return oldItem == newItem
    }
}