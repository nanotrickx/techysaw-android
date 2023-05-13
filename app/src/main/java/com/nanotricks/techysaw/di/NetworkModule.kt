package com.nanotricks.techysaw.di

import com.nanotricks.techysaw.data.repository.items.remote.TechysawApi
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

    // sample di implementation
    @Provides
    fun provideSampleText(): String {
        return "Sample text"
    }

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()    

    @Provides
    fun provideTechysawApi(): TechysawApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.1")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TechysawApi::class.java)

    }
    
}