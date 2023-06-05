package com.vijanthi.computervathiyar.di

import android.content.Context
import androidx.room.Room
import com.vijanthi.computervathiyar.data.db.TechysawDb
import com.vijanthi.computervathiyar.data.db.dao.CourseDao
import com.vijanthi.computervathiyar.data.repository.chapter.local.ChapterLocalSource
import com.vijanthi.computervathiyar.data.repository.chapter.local.ChapterMemorySource
import com.vijanthi.computervathiyar.data.repository.chapter.remote.ChapterApi
import com.vijanthi.computervathiyar.data.repository.chapter.remote.ChapterApiSource
import com.vijanthi.computervathiyar.data.repository.chapter.remote.ChapterRemoteSource
//import com.vijanthi.TechysawDb
import com.vijanthi.computervathiyar.data.repository.course.local.CourseLocalSource
import com.vijanthi.computervathiyar.data.repository.course.local.CourseMemorySource
import com.vijanthi.computervathiyar.data.repository.course.remote.CourseApi
import com.vijanthi.computervathiyar.data.repository.course.remote.CourseApiSource
import com.vijanthi.computervathiyar.data.repository.course.remote.CourseRemoteSource
import com.vijanthi.computervathiyar.data.repository.items.local.ItemsLocalSource
import com.vijanthi.computervathiyar.data.repository.items.local.ItemsMemorySource
import com.vijanthi.computervathiyar.data.repository.items.remote.TechysawApi
import com.vijanthi.computervathiyar.data.repository.items.remote.ItemsApiSource
import com.vijanthi.computervathiyar.data.repository.items.remote.ItemsRemoteSource
import com.vijanthi.computervathiyar.util.PrefManager
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
        return ChapterMemorySource(db.chapterDao(), db.courseDao())
    }

    @Provides
    @Singleton
    fun providesCourseDao(db: TechysawDb): CourseDao {
        return db.courseDao()
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