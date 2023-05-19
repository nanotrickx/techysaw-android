package com.nanotricks.techysaw.data.model

import com.google.gson.annotations.SerializedName

typealias CourseList = List<CourseItem>

data class CourseItem(
    @SerializedName("updatedAt")
    var updatedAt: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("slug")
    var slug: String,
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("description")
    var description: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("chapter")
    var chapter: List<Chapter>
)


data class Chapter(
    @SerializedName("src") var src: String,
    @SerializedName("status") var status: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("createdAt") var createdAt: String? = null,
    @SerializedName("chapterTitle") var chapterTitle: String,
    @SerializedName("updatedAt") var updatedAt: String? = null,
    @SerializedName("courseId") var courseId: String,
    @SerializedName("fileUrl") var fileUrl: String
)