package com.grigoryev.teya_home.core.app.data

import android.content.Context
import com.grigoryev.teya_home.album.detail.presentation.FormatDateUtil
import com.grigoryev.teya_home.core.util.GetStringUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PresentationModule {

    @Provides
    fun provideFormatDateUseCase(@ApplicationContext context: Context): FormatDateUtil {
        return FormatDateUtil(context)
    }
    
    @Provides
    fun provideGetRateMessageUtil(@ApplicationContext context: Context): GetStringUtil {
        return GetStringUtil(context)
    }
}