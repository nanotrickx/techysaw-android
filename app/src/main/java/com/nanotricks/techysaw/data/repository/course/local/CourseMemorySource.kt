package com.nanotricks.techysaw.data.repository.course.local

import com.nanotricks.techysaw.data.model.Course
import com.nanotricks.techysaw.data.model.CourseList
import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories

class CourseMemorySource : CourseLocalSource {

    private var cachedCourseList = mutableListOf<Course>()
    private var cacheCourseMap = HashMap<String, Course>()

    private var cachedAt = 0L // Nothing cached when starting, so set the time to 0 (oh, 1970! 😁)

    companion object {
        private const val SECOND_MILLIS = 1000
        private const val MINUTE_MILLIS = 60 * SECOND_MILLIS

        private const val CACHE_PERIOD_MILLIS = 10 * MINUTE_MILLIS /* 10 minutes cache */
    }

    override suspend fun isValid(): Boolean {
        return System.currentTimeMillis() - cachedAt < CACHE_PERIOD_MILLIS
    }

    override suspend fun saveCourse(courseList: CourseList) {
        if (courseList.isEmpty()) return
        cachedCourseList.apply {
            clear()
            addAll(courseList)
        }
        cachedAt = System.currentTimeMillis()
    }

    override suspend fun getCourses(): CourseList = cachedCourseList

    override suspend fun getCourseBySlug(slug: String): Course? {
        val course = cacheCourseMap[slug] ?: cachedCourseList.find {
            it.slug == slug
        }
        if (course != null && cacheCourseMap[slug] == null)
            cacheCourseMap[slug] = course
        return course
    }

    override suspend fun clearAll() {
        cachedCourseList.clear()
        cachedAt = 0L
    }
}