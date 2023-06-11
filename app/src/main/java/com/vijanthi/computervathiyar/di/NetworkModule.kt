package com.vijanthi.computervathiyar.di

import com.vijanthi.computervathiyar.data.repository.chapter.remote.ChapterApi
import com.vijanthi.computervathiyar.data.repository.course.remote.CourseApi
import com.vijanthi.computervathiyar.util.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    fun provideCourseApi(): CourseApi {
        return Retrofit.Builder()
            .baseUrl(Util.getApiUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CourseApi::class.java)
    }

    @Provides
    fun provideChapterApi(): ChapterApi {
        return Retrofit.Builder()
            .baseUrl(Util.getApiUrl())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChapterApi::class.java)
    }
}