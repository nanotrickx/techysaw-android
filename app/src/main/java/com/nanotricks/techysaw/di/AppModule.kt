package com.nanotricks.techysaw.di

import android.content.Context
import androidx.room.Room
import com.nanotricks.techysaw.data.db.TechysawDb
import com.nanotricks.techysaw.data.repository.chapter.local.ChapterLocalSource
import com.nanotricks.techysaw.data.repository.chapter.local.ChapterMemorySource
import com.nanotricks.techysaw.data.repository.chapter.remote.ChapterApi
import com.nanotricks.techysaw.data.repository.chapter.remote.ChapterApiSource
import com.nanotricks.techysaw.data.repository.chapter.remote.ChapterRemoteSource
//import com.nanotricks.techysaw.data.db.TechysawDb
import com.nanotricks.techysaw.data.repository.course.local.CourseLocalSource
import com.nanotricks.techysaw.data.repository.course.local.CourseMemorySource
import com.nanotricks.techysaw.data.repository.course.remote.CourseApi
import com.nanotricks.techysaw.data.repository.course.remote.CourseApiSource
import com.nanotricks.techysaw.data.repository.course.remote.CourseRemoteSource
import com.nanotricks.techysaw.data.repository.items.local.ItemsLocalSource
import com.nanotricks.techysaw.data.repository.items.local.ItemsMemorySource
import com.nanotricks.techysaw.data.repository.items.remote.TechysawApi
import com.nanotricks.techysaw.data.repository.items.remote.ItemsApiSource
import com.nanotricks.techysaw.data.repository.items.remote.ItemsRemoteSource
import com.nanotricks.techysaw.util.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesItemsRemoteSource(appApi: TechysawApi): ItemsRemoteSource {
        return ItemsApiSource(appApi)
    }

    @Provides
    @Singleton
    fun providesItemsLocalSource(): ItemsLocalSource {
        return ItemsMemorySource()
    }

    @Provides
    @Singleton
    fun providesCourseLocalSource(db: TechysawDb, prefManager: PrefManager): CourseLocalSource {
        return CourseMemorySource(db.courseDao(), prefManager)
    }

    @Provides
    fun providesCourseRemoteSource(appApi: CourseApi): CourseRemoteSource {
        return CourseApiSource(appApi)
    }

    @Provides
    @Singleton
    fun providesChapterLocalSource(db: TechysawDb): ChapterLocalSource {
        return ChapterMemorySource(db.chapterDao())
    }

    @Provides
    fun providesChapterRemoteSource(appApi: ChapterApi): ChapterRemoteSource {
        return ChapterApiSource(appApi)
    }

    @Singleton
    @Provides
    fun provideRoomDb(@ApplicationContext context: Context): TechysawDb {
        return Room.databaseBuilder(
            context.applicationContext,
            TechysawDb::class.java,
            "techysaw_database"
        ).fallbackToDestructiveMigration().build()
    }

}