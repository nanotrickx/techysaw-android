package com.vijanthi.computervathiyar.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("course_read_status")
data class CourseReadStatus(
    @PrimaryKey
    val slug: String,
    val courseId: String ,
    val readTime: Long= System.currentTimeMillis(),
    var readCount: Int = 1
)
