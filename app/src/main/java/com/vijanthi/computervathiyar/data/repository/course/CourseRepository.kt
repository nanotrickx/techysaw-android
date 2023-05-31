package com.vijanthi.computervathiyar.data.repository.course

import com.vijanthi.computervathiyar.data.model.Chapter
import com.vijanthi.computervathiyar.data.model.Course
import com.vijanthi.computervathiyar.data.model.Resource
import com.vijanthi.computervathiyar.data.repository.course.local.CourseLocalSource
import com.vijanthi.computervathiyar.data.repository.course.remote.CourseRemoteSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepository @Inject constructor(
    private val remoteSource: CourseRemoteSource,
    private val localSource: CourseLocalSource
) {

    fun updateChapterRead(course: Course, chapter: Chapter) {
        localSource.setReadChapter(course, chapter)
    }

    fun getCourseBySlug(slug: String) = flow {
        if (localSource.isValid().not()) {
            getAllCourses().collect()
        }
        if (localSource.isValid()) {
            val chapterReadList = localSource.getReadChapter(slug)
            val course = localSource.getCourseBySlug(slug)
            if (course!= null) {
                if (chapterReadList.isNullOrEmpty().not())
                    course.chapter.forEach {
                        it.isRead = chapterReadList?.contains(it.slug) ?: false
                    }
                emit(Resource.Success(course))
                return@flow
            }
        }
        emit(Resource.Error(IllegalStateException("Local source not found")))
    }

    fun filterCourse(searchText: String) = flow {

        // Use the local cached copy if not forcing
        if (localSource.isValid()) {
            emit(Resource.Success(localSource.getCourses().filter {
                it.title.lowercase() == searchText || it.title.lowercase().startsWith(searchText)
            }))
            return@flow
        } else { // Force, clear the data
            localSource.clearAll()
        }

        when (val resource = remoteSource.getAllCourse()) {

            Resource.None -> {}

            is Resource.Error -> emit(resource)

            is Resource.Success -> {

                emit(
                    Resource.Success(resource.data.filter {
                        it.title.lowercase() == searchText || it.title.lowercase().startsWith(searchText)
                    })
                )

                // save a local copy
//                localSource.saveTopStories(resource.data)
            }
        }

    }
    fun getAllCourses() = flow {

        // Use the local cached copy if not forcing
        if (localSource.isValid()) {
            emit(Resource.Success(localSource.getCourses()))
            return@flow
        } else { // Force, clear the data
            localSource.clearAll()
        }

        when (val resource = remoteSource.getAllCourse()) {

            Resource.None -> {}

            is Resource.Error -> emit(resource)

            is Resource.Success -> {
                localSource.saveCourse(resource.data)
                emit(
                    Resource.Success(resource.data)
                )

                // save a local copy
//                localSource.saveTopStories(resource.data)
            }
        }

    }

}