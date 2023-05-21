package com.nanotricks.techysaw.data.repository.chapter.remote

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ChapterApi {

    @GET
    suspend fun getChapter(@Url url: String): Response<JsonObject>

}