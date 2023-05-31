package com.vijanthi.computervathiyar.data.repository.course.remote

import com.vijanthi.computervathiyar.data.model.CourseList
import retrofit2.Response
import retrofit2.http.GET

interface CourseApi {

    @GET("courses")
    suspend fun getAllCourses(): Response<CourseList>

}