package com.nanotricks.techysaw.data.repository.items.remote

import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories
import com.nanotricks.techysaw.data.model.Resource

interface ItemsRemoteSource {

    suspend fun getTopStories(): Resource<HNTopStories>

    suspend fun getItemDetails(itemId: Long): Resource<HNItem>
}