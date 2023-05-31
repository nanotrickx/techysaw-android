package com.vijanthi.computervathiyar.data.repository.chapter.local

import com.vijanthi.computervathiyar.data.db.dao.ChapterDao
import com.vijanthi.computervathiyar.data.db.model.ChapterData
import com.vijanthi.computervathiyar.data.db.model.toChapterData
import com.vijanthi.computervathiyar.data.model.Chapter

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