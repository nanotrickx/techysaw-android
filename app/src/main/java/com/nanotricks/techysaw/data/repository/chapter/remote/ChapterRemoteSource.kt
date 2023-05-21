package com.nanotricks.techysaw.data.repository.chapter.remote

import com.google.gson.JsonObject
import com.nanotricks.techysaw.data.model.Resource

interface ChapterRemoteSource {
    suspend fun getChapter(url: String): Resource<JsonObject>
}