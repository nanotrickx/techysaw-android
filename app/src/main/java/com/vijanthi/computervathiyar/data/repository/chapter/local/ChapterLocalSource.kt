package com.vijanthi.computervathiyar.data.repository.chapter.local

import com.vijanthi.computervathiyar.data.db.model.ChapterData
import com.vijanthi.computervathiyar.data.model.Chapter

interface ChapterLocalSource {

    suspend fun saveChapter(chapter: Chapter, data: String)

    suspend fun getChapter(slug: String): ChapterData?

    suspend fun clearAll()
}