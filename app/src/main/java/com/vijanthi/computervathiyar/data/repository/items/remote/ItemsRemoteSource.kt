package com.vijanthi.computervathiyar.data.repository.items.remote

import com.vijanthi.computervathiyar.data.model.HNItem
import com.vijanthi.computervathiyar.data.model.HNTopStories
import com.vijanthi.computervathiyar.data.model.Resource

interface ItemsRemoteSource {

    suspend fun getTopStories(): Resource<HNTopStories>

    suspend fun getItemDetails(itemId: Long): Resource<HNItem>
}