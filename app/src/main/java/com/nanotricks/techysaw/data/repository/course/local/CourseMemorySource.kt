package com.nanotricks.techysaw.data.repository.course.local

import android.util.Log
import com.nanotricks.techysaw.data.db.dao.CourseDao
import com.nanotricks.techysaw.data.db.model.CourseReadStatus
import com.nanotricks.techysaw.data.model.Chapter
import com.nanotricks.techysaw.data.model.Course
import com.nanotricks.techysaw.data.model.CourseList
import com.nanotricks.techysaw.data.model.HNItem
import com.nanotricks.techysaw.data.model.HNTopStories
import com.nanotricks.techysaw.util.PrefManager

class CourseMemorySource(private val courseDao: CourseDao, private val prefManager: PrefManager) : CourseLocalSource {

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

    override fun setReadChapter(course: Course, chapter: Chapter) {
        val courseReadStatus = prefManager.courseChapterRead
        if(courseReadStatus.contains(chapter.slug)) {
            return
        }
        val updatedList = mutableListOf(chapter.slug!!)
        updatedList.addAll(courseReadStatus)
        prefManager.courseChapterRead = updatedList.toList()
        Log.d("cms", "setReadChapter() called with: chapter = ${prefManager.courseChapterRead}")
    }

    override suspend fun getReadChapter(courseId: String): List<String> = prefManager.courseChapterRead

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