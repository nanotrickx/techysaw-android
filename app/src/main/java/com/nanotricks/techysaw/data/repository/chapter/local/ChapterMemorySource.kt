package com.nanotricks.techysaw.data.repository.chapter.local

import com.nanotricks.techysaw.data.db.TechysawDb
import com.nanotricks.techysaw.data.db.dao.ChapterDao
import com.nanotricks.techysaw.data.db.model.ChapterData
import com.nanotricks.techysaw.data.db.model.toChapterData
import com.nanotricks.techysaw.data.model.Chapter
import java.util.Date

class ChapterMemorySource(private val db: ChapterDao): ChapterLocalSource {

    private val chapterMap: MutableMap<String, ChapterData> = mutableMapOf()

    override suspend fun saveChapter(cd: Chapter, data: String) {
        chapterMap[cd.slug!!] = ChapterData(cd.slug!!, data = data)
        db.addChapter(cd.toChapterData(data))
    }

    override suspend fun getChapter(slug: String): ChapterData? {
        return db.getChapter(slug)
    }

    override suspend fun clearAll() {
        chapterMap.clear()
    }
}