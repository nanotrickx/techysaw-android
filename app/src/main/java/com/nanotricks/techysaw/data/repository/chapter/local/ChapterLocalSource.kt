package com.nanotricks.techysaw.data.repository.chapter.local

import com.nanotricks.techysaw.data.db.model.ChapterData
import com.nanotricks.techysaw.data.model.Chapter
import com.nanotricks.techysaw.data.model.Course

interface ChapterLocalSource {
    suspend fun isValid(): Boolean

    suspend fun saveChapter(slug: String, data: String)

    suspend fun getChapter(slug: String): ChapterData?

    suspend fun clearAll()
}