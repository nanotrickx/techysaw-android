package com.nanotricks.techysaw.data.repository.chapter.remote

import com.google.gson.JsonObject
import com.nanotricks.techysaw.data.model.Resource
import retrofit2.Response
import javax.inject.Inject

class ChapterApiSource @Inject constructor(private val chapterApi: ChapterApi) : ChapterRemoteSource {
    private suspend fun <T : Any> withExceptionHandling(block: suspend () -> Response<T>): Resource<T> {

        return try {
            val response = block()

            if (response.isSuccessful) {
                Resource.Success(response.body() as T)
            } else {
                Resource.Error(Throwable(response.message() ?: response.code().toString()))
            }

        } catch (e: Exception) {
            Resource.Error(Throwable(e.toString()))
        }
    }

    override suspend fun getChapter(url:String): Resource<JsonObject> = withExceptionHandling {
        chapterApi.getChapter(url)
    }
}