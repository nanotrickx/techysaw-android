package com.nanotricks.techysaw.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nanotricks.techysaw.data.db.model.CourseReadStatus

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourseRead(courseReadStatus: CourseReadStatus)

    @Query("SELECT readList FROM course_read_status WHERE courseId = :courseId")
    suspend fun getCourseReadChapters(courseId: String): List<String>
}