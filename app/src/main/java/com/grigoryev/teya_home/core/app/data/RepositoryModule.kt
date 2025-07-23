package com.grigoryev.teya_home.core.app.data

import com.grigoryev.teya_home.album.list.data.AlbumRepository
import com.grigoryev.teya_home.album.list.data.AlbumRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository
    
    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository
    
    @Binds
    @Singleton
    abstract fun bindConnectionRepository(
        connectionRepositoryImpl: ConnectionRepositoryImpl
    ): ConnectionRepository
} 