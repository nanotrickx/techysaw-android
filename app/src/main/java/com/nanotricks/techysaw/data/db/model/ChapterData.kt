package com.nanotricks.techysaw.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nanotricks.techysaw.data.model.Chapter
import java.util.Date

@Entity(tableName = "chapter_data")
data class ChapterData(
    @PrimaryKey(autoGenerate = false)
    val slug: String,
    val date: String = Date().toString(),
    val chapterId: String = "",
    val chapterNo: Int = 0,
    val data: String
)

//class ChapterData(): RealmObject {
//    var slug: String = ""
//    var date: String = Date().toString()
//    var data: String = ""
//
//    constructor(slugId: String, chapterData: String) : this() {
//        slug = slugId
//        data = chapterData
//    }
//}

//@Entity(tableName = "note_table")
//data class Note(val title: String,
//                val description: String,
//                val priority: Int,
//                @PrimaryKey(autoGenerate = false) val id: Int? = null)

fun Chapter.toChapterData(data: String): ChapterData {
    return ChapterData(slug!!, chapterId = this.courseId, chapterNo = 1, data = data)
}