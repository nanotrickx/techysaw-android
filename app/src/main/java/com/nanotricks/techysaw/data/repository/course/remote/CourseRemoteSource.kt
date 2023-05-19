package com.nanotricks.techysaw.data.repository.course.remote

import com.nanotricks.techysaw.data.model.CourseList
import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories
import com.nanotricks.techysaw.data.model.Resource

interface CourseRemoteSource {

    suspend fun getAllCourse(): Resource<CourseList>

}