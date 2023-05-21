package com.nanotricks.techysaw.data.repository.chapter.local

import com.nanotricks.techysaw.data.db.model.ChapterData
import com.nanotricks.techysaw.data.model.Chapter
import java.util.Date

class ChapterMemorySource: ChapterLocalSource {

    private val chapterMap: MutableMap<String, ChapterData> = mutableMapOf()

    override suspend fun isValid(): Boolean {
        return chapterMap.isNotEmpty()
    }

    override suspend fun saveChapter(slug: String, data: String) {
        chapterMap[slug] = ChapterData(slug, Date(), data)
    }

    override suspend fun getChapter(slug: String): ChapterData? = chapterMap.getOrDefault(slug, null)


    override suspend fun clearAll() {
        chapterMap.clear()
    }
}