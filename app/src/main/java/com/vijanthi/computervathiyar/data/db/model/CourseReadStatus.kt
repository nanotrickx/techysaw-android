package com.vijanthi.computervathiyar.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("course_read_status")
data class CourseReadStatus(
    @PrimaryKey
    val courseId: String,
    val readList: ArrayList<String>
)
