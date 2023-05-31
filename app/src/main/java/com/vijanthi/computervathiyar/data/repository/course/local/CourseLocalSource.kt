package com.vijanthi.computervathiyar.data.repository.course.local

import com.vijanthi.computervathiyar.data.model.Chapter
import com.vijanthi.computervathiyar.data.model.Course
import com.vijanthi.computervathiyar.data.model.CourseList

interface CourseLocalSource {

    suspend fun isValid(): Boolean

    suspend fun saveCourse(courseList: CourseList)

    fun setReadChapter(course: Course, chapter: Chapter)
    suspend fun getReadChapter(courseId: String): List<String>?
    suspend fun getCourses(): CourseList

    suspend fun getCourseBySlug(slug: String): Course?

    suspend fun clearAll()
}