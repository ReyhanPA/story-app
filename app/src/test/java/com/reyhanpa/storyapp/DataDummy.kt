package com.reyhanpa.storyapp

import com.reyhanpa.storyapp.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyListStory(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                "author + $i",
                "quote $i",
            )
            items.add(quote)
        }
        return items
    }
}
