package com.vijanthi.computervathiyar.data.repository.chapter.local

import android.util.Log
import com.vijanthi.computervathiyar.data.db.dao.ChapterDao
import com.vijanthi.computervathiyar.data.db.dao.CourseDao
import com.vijanthi.computervathiyar.data.db.model.ChapterData
import com.vijanthi.computervathiyar.data.db.model.CourseReadStatus
import com.vijanthi.computervathiyar.data.db.model.toChapterData
import com.vijanthi.computervathiyar.data.model.Chapter
import com.vijanthi.computervathiyar.util.TAG

class ChapterMemorySource(private val db: ChapterDao, private val courseDao: CourseDao): ChapterLocalSource {

    override suspend fun saveChapter(chapter: Chapter, data: String) {
        Log.d(TAG, "saveChapter() called with: chapter = ${chapter.slug}, data =")
        db.addChapter(chapter.toChapterData(data))
        courseDao.insertCourseRead(CourseReadStatus(chapter.slug!!, chapter.courseId))
    }

    override suspend fun getChapter(slug: String): ChapterData? {
        val crc = courseDao.getCourseReadChapters(slug)
        if (crc != null) {
            crc.readCount++
            courseDao.insertCourseRead(crc)
        }
        return db.getChapter(slug)
    }

    override suspend fun clearAll() {
        db.clear()
    }
}