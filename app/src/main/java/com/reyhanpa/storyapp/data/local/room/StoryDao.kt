package com.reyhanpa.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reyhanpa.storyapp.data.local.entity.ListStoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int, ListStoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(events: List<ListStoryEntity>)

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}