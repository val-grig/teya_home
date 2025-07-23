package com.grigoryev.teya_home.core.app.data

import com.grigoryev.teya_home.album.list.data.AlbumRepository
import com.grigoryev.teya_home.album.list.data.AlbumRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository
} 