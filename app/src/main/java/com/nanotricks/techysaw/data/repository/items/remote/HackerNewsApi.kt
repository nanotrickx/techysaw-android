package com.nanotricks.techysaw.data.repository.items.remote

import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Official HN API documentation: https://github.com/Techysaw/API
interface TechysawApi {

    @GET("topstories.json")
    suspend fun getTopStories(): Response<HNTopStories>

    @GET("item/{itemId}.json")
    suspend fun getItemDetails(
        @Path("itemId") itemId: Long
    ): Response<HNItem>

}