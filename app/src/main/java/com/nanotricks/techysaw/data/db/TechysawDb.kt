package com.nanotricks.techysaw.data.db

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nanotricks.techysaw.data.db.dao.ChapterDao
import com.nanotricks.techysaw.data.db.dao.CourseDao
import com.nanotricks.techysaw.data.db.model.ChapterData
import com.nanotricks.techysaw.data.db.model.CourseReadStatus
import com.nanotricks.techysaw.util.fromJson

//
@Database(entities = [ChapterData::class, CourseReadStatus::class], version = 1, exportSchema = false)
@TypeConverters(ArrayListConverter::class)
abstract class TechysawDb : RoomDatabase() {
    abstract fun chapterDao(): ChapterDao
    abstract fun courseDao(): CourseDao
}



class ArrayListConverter {

    @TypeConverter
    fun fromStringArrayList(value: ArrayList<String>): String {
        Log.d("ArrayListConverter", "fromStringArrayList() called with: value = $value")
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringArrayList(value: String): ArrayList<String> {
        Log.d("ArrayListConverter", "toStringArrayList() called with: value = $value")
        return try {
            Gson().fromJson<ArrayList<String>>(value) //using extension function
        } catch (e: Exception) {
            arrayListOf()
        }
    }
}