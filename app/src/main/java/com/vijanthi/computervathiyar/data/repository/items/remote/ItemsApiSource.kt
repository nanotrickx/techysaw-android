package com.vijanthi.computervathiyar.data.repository.items.remote

import com.vijanthi.computervathiyar.data.model.HNItem
import com.vijanthi.computervathiyar.data.model.HNTopStories
import com.vijanthi.computervathiyar.data.model.Resource
import retrofit2.Response
import javax.inject.Inject


class ItemsApiSource @Inject constructor(
    private val hackerNewsApi: TechysawApi
) : ItemsRemoteSource {


    override suspend fun getTopStories(): Resource<HNTopStories> = withExceptionHandling {
        hackerNewsApi.getTopStories()
    }


    override suspend fun getItemDetails(itemId: Long): Resource<HNItem> = withExceptionHandling {
        hackerNewsApi.getItemDetails(itemId = itemId)
    }


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
}

