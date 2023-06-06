package com.vijanthi.computervathiyar.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vijanthi.computervathiyar.data.db.model.ChapterData


@Dao
interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChapter(chapterData: ChapterData)

    @Query("SELECT * FROM chapter_data WHERE slug = :slug")
    suspend fun getChapter(slug: String): ChapterData?

    @Query("DELETE FROM chapter_data")
    suspend fun clear(): Int

}