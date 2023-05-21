package com.nanotricks.techysaw.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nanotricks.techysaw.data.db.model.ChapterData


@Dao
interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChapter(chapterData: ChapterData)

    @Query("SELECT * FROM chapter_data WHERE slug = :slug")
    suspend fun getChapter(slug: String): ChapterData?

}