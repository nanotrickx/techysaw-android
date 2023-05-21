package com.nanotricks.techysaw.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "chapter_data")
data class ChapterData(
    @PrimaryKey(autoGenerate = false)
    val slug: String,
    val date: Date,
    val data: String
)

//@Entity(tableName = "note_table")
//data class Note(val title: String,
//                val description: String,
//                val priority: Int,
//                @PrimaryKey(autoGenerate = false) val id: Int? = null)
