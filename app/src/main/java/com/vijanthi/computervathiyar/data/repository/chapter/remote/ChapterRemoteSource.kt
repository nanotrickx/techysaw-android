package com.vijanthi.computervathiyar.data.repository.chapter.remote

import com.google.gson.JsonObject
import com.vijanthi.computervathiyar.data.model.Resource

interface ChapterRemoteSource {
    suspend fun getChapter(url: String): Resource<JsonObject>
}