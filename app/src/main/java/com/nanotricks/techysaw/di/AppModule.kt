package com.nanotricks.techysaw.di

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

}