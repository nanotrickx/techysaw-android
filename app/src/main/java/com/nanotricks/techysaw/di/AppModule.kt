package com.nanotricks.techysaw.di

import com.nanotricks.techysaw.data.repository.course.remote.CourseApi
import com.nanotricks.techysaw.data.repository.course.remote.CourseApiSource
import com.nanotricks.techysaw.data.repository.course.remote.CourseRemoteSource
import com.nanotricks.techysaw.data.repository.items.local.ItemsLocalSource
import com.nanotricks.techysaw.data.repository.items.local.ItemsMemorySource
import com.nanotricks.techysaw.data.repository.items.remote.TechysawApi
import com.nanotricks.techysaw.data.repository.items.remote.ItemsApiSource
import com.nanotricks.techysaw.data.repository.items.remote.ItemsRemoteSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun providesCourseRemoteSource(appApi: CourseApi): CourseRemoteSource {
        return CourseApiSource(appApi)
    }

}