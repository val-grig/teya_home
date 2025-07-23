package com.grigoryev.teya_home.album.list.domain

import com.grigoryev.teya_home.core.app.data.PreferencesRepository
import javax.inject.Inject

class SaveRateUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend fun invoke(rating: Int) {
        preferencesRepository.setAppRating(rating)
    }
} 