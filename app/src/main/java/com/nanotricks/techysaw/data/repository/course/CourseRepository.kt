package com.nanotricks.techysaw.data.repository.course

import com.nanotricks.techysaw.data.model.Resource
import com.nanotricks.techysaw.data.repository.course.remote.CourseRemoteSource
import com.nanotricks.techysaw.data.repository.items.local.ItemsLocalSource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepository @Inject constructor(
    private val remoteSource: CourseRemoteSource,
    private val localSource: ItemsLocalSource
) {
    fun getAllCourses() = flow {

        // Use the local cached copy if not forcing
        if (localSource.isValid()) {
//            emit(Resource.Success(localSource.getTopStories()))
            return@flow
        } else { // Force, clear the data
            localSource.clearAll()
        }

        when (val resource = remoteSource.getAllCourse()) {

            Resource.None -> {}

            is Resource.Error -> emit(resource)

            is Resource.Success -> {

                emit(
                    Resource.Success(resource.data)
                )

                // save a local copy
//                localSource.saveTopStories(resource.data)
            }
        }

    }

}