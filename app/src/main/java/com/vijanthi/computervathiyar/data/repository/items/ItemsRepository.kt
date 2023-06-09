package com.vijanthi.computervathiyar.data.repository.items

import com.vijanthi.computervathiyar.data.model.HNItem
import com.vijanthi.computervathiyar.data.model.Resource
import com.vijanthi.computervathiyar.data.repository.items.local.ItemsLocalSource
import com.vijanthi.computervathiyar.data.repository.items.remote.ItemsRemoteSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemsRepository @Inject constructor(
    private val remoteSource: ItemsRemoteSource,
    private val localSource: ItemsLocalSource
) {

    fun getTopStories(force: Boolean = false) = flow {

        // Use the local cached copy if not forcing
        if (!force && localSource.isValid()) {
            emit(Resource.Success(localSource.getTopStories()))
            return@flow
        } else { // Force, clear the data
            localSource.clearAll()
        }

        when (val resource = remoteSource.getTopStories()) {

            Resource.None -> {}

            is Resource.Error -> emit(resource)

            is Resource.Success -> {

                emit(
                    Resource.Success(resource.data)
                )

                // save a local copy
                localSource.saveTopStories(resource.data)

            }
        }

    }

    suspend fun fetchItems(ids: List<Long>, force: Boolean = false) = flow {

        val map = mutableMapOf<Long, HNItem>()

        coroutineScope {

            ids.forEach { itemId ->

                launch {
                    val item = getItemDetails(itemId, force = force)

                    item?.let { hnItem ->

                        // Keep a local copy for future use
                        localSource.saveItem(hnItem)

                        map.putIfAbsent(item.id, item)
                    }
                }
            }
        }

        val list = ids.mapNotNull { map[it] }

        if (ids.size == list.size) {
            emit(Resource.Success(list.toList()))
        } else {
            emit(Resource.Error(Throwable("Unable to fetch items at this time")))
        }

    }

    suspend fun getItemDetails(itemId: Long, force: Boolean = false): HNItem? {

        // Use the local cached copy if not forcing
        if (!force && localSource.isValid()) {
            val item = localSource.getItemById(itemId = itemId)
            if (item != null) return item
        }


        return when (val res = remoteSource.getItemDetails(itemId = itemId)) {

            is Resource.Error,
            Resource.None -> null

            is Resource.Success -> {
                localSource.saveItem(res.data)
                res.data
            }
        }

    }

}