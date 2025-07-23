package com.grigoryev.teya_home.core.app.data

import android.content.Context
import com.grigoryev.teya_home.album.detail.presentation.FormatDateUtil
import com.grigoryev.teya_home.album.detail.presentation.AlbumDetailScreenMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {
    
    @Provides
    @Singleton
    fun provideFormatDateUseCase(@ApplicationContext context: Context): FormatDateUtil {
        return FormatDateUtil(context)
    }
    
    @Provides
    @Singleton
    fun provideAlbumDetailScreenMapper(formatDateUtil: FormatDateUtil): AlbumDetailScreenMapper {
        return AlbumDetailScreenMapper(formatDateUtil)
    }
} 