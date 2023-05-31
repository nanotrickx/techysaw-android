package com.vijanthi.computervathiyar.di

import com.vijanthi.computervathiyar.data.repository.chapter.remote.ChapterApi
import com.vijanthi.computervathiyar.data.repository.course.remote.CourseApi
import com.vijanthi.computervathiyar.data.repository.items.remote.TechysawApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    fun provideTechysawApi(): TechysawApi {
        return Retrofit.Builder()
            .baseUrl("https://wtyxus64y5.execute-api.ap-south-1.amazonaws.com/dev/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TechysawApi::class.java)
    }

    @Provides
    fun provideCourseApi(): CourseApi {
        return Retrofit.Builder()
            .baseUrl("https://wtyxus64y5.execute-api.ap-south-1.amazonaws.com/dev/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CourseApi::class.java)
    }

    @Provides
    fun provideChapterApi(): ChapterApi {
        return Retrofit.Builder()
            .baseUrl("https://wtyxus64y5.execute-api.ap-south-1.amazonaws.com/dev/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChapterApi::class.java)
    }
}