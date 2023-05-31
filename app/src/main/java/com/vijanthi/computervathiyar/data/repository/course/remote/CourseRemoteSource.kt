package com.vijanthi.computervathiyar.data.repository.course.remote

import com.vijanthi.computervathiyar.data.model.CourseList
import com.vijanthi.computervathiyar.data.model.Resource

interface CourseRemoteSource {

    suspend fun getAllCourse(): Resource<CourseList>

}