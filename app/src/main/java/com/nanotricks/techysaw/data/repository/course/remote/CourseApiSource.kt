package com.nanotricks.techysaw.data.repository.course.remote

import com.nanotricks.techysaw.data.model.CourseList
import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories
import com.nanotricks.techysaw.data.model.Resource
import retrofit2.Response
import javax.inject.Inject


class CourseApiSource @Inject constructor(
    private val courseApi: CourseApi
) : CourseRemoteSource {



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

    override suspend fun getAllCourse(): Resource<CourseList> = withExceptionHandling {
        courseApi.getAllCourses()
    }
}

