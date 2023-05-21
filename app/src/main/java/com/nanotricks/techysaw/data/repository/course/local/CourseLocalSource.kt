package com.nanotricks.techysaw.data.repository.course.local

import com.nanotricks.techysaw.data.model.Course
import com.nanotricks.techysaw.data.model.CourseList
import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories

interface CourseLocalSource {

    suspend fun isValid(): Boolean

    suspend fun saveCourse(courseList: CourseList)

    suspend fun getCourses(): CourseList

    suspend fun getCourseBySlug(slug: String): Course?

    suspend fun clearAll()
}