package com.nanotricks.techysaw.data.repository.items.local

import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories

interface ItemsLocalSource {

    suspend fun isValid(): Boolean

    suspend fun saveTopStories(stories: HNTopStories)

    suspend fun getTopStories(): HNTopStories

    suspend fun saveItem(item: HNItem)

    suspend fun saveItems(items: List<HNItem>)

    suspend fun getItemById(itemId: Long): HNItem?

    suspend fun getItemsById(list: List<Long>): List<HNItem?>

    suspend fun clearAll()
}