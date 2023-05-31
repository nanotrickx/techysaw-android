package com.vijanthi.computervathiyar.domain

import com.vijanthi.computervathiyar.data.model.Resource
import com.vijanthi.computervathiyar.data.repository.items.ItemsRepository
import com.vijanthi.computervathiyar.domain.model.HNItemWithTimeAgo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetChapterFromUrl(
    private val repository: ItemsRepository,
    default: String = "sometime ago"
) {
    val timeAgoUseCase: GetTimeAgoUseCase = GetTimeAgoUseCase(default = default)

    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
        private const val DAY_MILLIS = 24 * HOUR_MILLIS
    }

    suspend operator fun invoke(list: List<Long>, force: Boolean = false): Flow<Resource<List<HNItemWithTimeAgo>>> =
        repository.fetchItems(list, force = force).map {

            when (it) {
                is Resource.Success -> {
                    val tra = it.data.map { item -> HNItemWithTimeAgo(item, timeAgoUseCase(item.time)) }
                    Resource.Success(tra)
                }
                is Resource.Error -> {
                    Resource.Error(it.throwable)
                }
                else -> {
                    Resource.None
                }
            }
        }
}