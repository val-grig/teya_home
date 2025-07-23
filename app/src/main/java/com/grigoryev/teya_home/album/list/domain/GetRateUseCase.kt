package com.grigoryev.teya_home.album.list.domain

import com.grigoryev.teya_home.core.app.data.PreferencesRepository
import javax.inject.Inject

class GetRateUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    fun invoke(): Int {
        return preferencesRepository.getAppRating()
    }
} 