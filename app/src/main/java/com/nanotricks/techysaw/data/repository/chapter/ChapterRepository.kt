package com.nanotricks.techysaw.data.repository.chapter

import com.google.gson.Gson
import com.nanotricks.techysaw.data.db.model.ChapterData
import com.nanotricks.techysaw.data.model.Chapter
import com.nanotricks.techysaw.data.model.Resource
import com.nanotricks.techysaw.data.repository.chapter.local.ChapterLocalSource
import com.nanotricks.techysaw.data.repository.chapter.remote.ChapterRemoteSource
import com.nanotricks.techysaw.data.repository.course.local.CourseLocalSource
import com.nanotricks.techysaw.data.repository.course.remote.CourseRemoteSource
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChapterRepository @Inject constructor(
    private val remoteSource: ChapterRemoteSource,
    private val localSource: ChapterLocalSource
) {
    fun getChapterData(chapter: Chapter) = flow {
        // Use the local cached copy if not forcing
        if (localSource.isValid() && localSource.getChapter(chapter.slug!!) != null) {
            emit(Resource.Success(localSource.getChapter(chapter.slug!!)!!))
            return@flow
        } else { // Force, clear the data
            localSource.clearAll()
        }

        when (val resource = remoteSource.getChapter(chapter.fileUrl)) {

            Resource.None -> {}

            is Resource.Error -> emit(resource)

            is Resource.Success -> {

                localSource.saveChapter(chapter.slug!!, Gson().toJson(resource.data))
                emit(
                    Resource.Success(ChapterData(chapter.slug!!, Date(),Gson().toJson(resource.data)))
                )

                // save a local copy
//                localSource.saveTopStories(resource.data)
            }
        }

    }
}