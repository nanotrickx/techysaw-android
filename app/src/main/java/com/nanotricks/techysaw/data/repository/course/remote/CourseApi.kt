package com.nanotricks.techysaw.data.repository.course.remote

import com.nanotricks.techysaw.data.model.CourseList
import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CourseApi {

    @GET("courses")
    suspend fun getAllCourses(): Response<CourseList>

}