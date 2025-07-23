package com.grigoryev.teya_home.core.di

import android.content.Context
import com.grigoryev.teya_home.album.list.data.dao.AlbumDao
import com.grigoryev.teya_home.album.list.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideAlbumDao(database: AppDatabase): AlbumDao {
        return database.albumDao()
    }
} 