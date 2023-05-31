package com.vijanthi.computervathiyar.data.repository.chapter

import com.google.gson.Gson
import com.vijanthi.computervathiyar.data.db.model.ChapterData
import com.vijanthi.computervathiyar.data.model.Chapter
import com.vijanthi.computervathiyar.data.model.Resource
import com.vijanthi.computervathiyar.data.repository.chapter.local.ChapterLocalSource
import com.vijanthi.computervathiyar.data.repository.chapter.remote.ChapterRemoteSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChapterRepository @Inject constructor(
    private val remoteSource: ChapterRemoteSource,
    private val localSource: ChapterLocalSource
) {
    fun getChapterData(chapter: Chapter) = flow {
        // Use the local cached copy if not forcing
        if (localSource.getChapter(chapter.slug!!) != null) {
            emit(Resource.Success(localSource.getChapter(chapter.slug!!)!!))
            return@flow
        } else { // Force, clear the data
            localSource.clearAll()
        }

        when (val resource = remoteSource.getChapter(chapter.fileUrl)) {

            Resource.None -> {}

            is Resource.Error -> emit(resource)

            is Resource.Success -> {

                localSource.saveChapter(chapter, Gson().toJson(resource.data))
                emit(
                    Resource.Success(ChapterData(chapter.slug!!, data = Gson().toJson(resource.data)))
                )

                // save a local copy
//                localSource.saveTopStories(resource.data)
            }
        }

    }
}