package com.vijanthi.computervathiyar.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vijanthi.computervathiyar.data.db.model.CourseReadStatus

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourseRead(courseReadStatus: CourseReadStatus)

    @Query("SELECT * FROM course_read_status WHERE slug = :slug")
    suspend fun getCourseReadChapters(slug: String): CourseReadStatus?
}