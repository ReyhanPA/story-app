package com.reyhanpa.storyapp.utils

import com.reyhanpa.storyapp.data.local.entity.ListStoryEntity
import com.reyhanpa.storyapp.data.remote.response.ListStoryItem

fun ListStoryEntity.toStoryItem(): ListStoryItem {
    return ListStoryItem(
        id = this.id,
        name = this.name,
        description = this.description,
        photoUrl = this.photoUrl,
        createdAt = this.createdAt,
        lat = this.lat,
        lon = this.lon
    )
}