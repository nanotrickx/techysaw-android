package com.nanotricks.techysaw.data.repository.course.local

import com.nanotricks.techysaw.data.model.Chapter
import com.nanotricks.techysaw.data.model.Course
import com.nanotricks.techysaw.data.model.CourseList
import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories

interface CourseLocalSource {

    suspend fun isValid(): Boolean

    suspend fun saveCourse(courseList: CourseList)

    fun setReadChapter(course: Course, chapter: Chapter)
    suspend fun getReadChapter(courseId: String): List<String>?
    suspend fun getCourses(): CourseList

    suspend fun getCourseBySlug(slug: String): Course?

    suspend fun clearAll()
}