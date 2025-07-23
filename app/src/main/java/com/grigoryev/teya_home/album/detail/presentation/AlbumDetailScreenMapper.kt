package com.grigoryev.teya_home.album.detail.presentation

import com.grigoryev.teya_home.album.detail.presentation.FormatDateUtil
import com.grigoryev.teya_home.core.mvi.StateScreenMapper
import javax.inject.Inject

class AlbumDetailScreenMapper @Inject constructor(
    private val formatDateUtil: FormatDateUtil
) : StateScreenMapper<AlbumDetailModelState, AlbumDetailUIState>() {
    override fun map(modelState: AlbumDetailModelState): AlbumDetailUIState {
        val album = modelState.album
        return AlbumDetailUIState(
            title = album.title,
            artist = album.artist,
            coverUrl = album.coverUrl,
            releaseDate = formatDateUtil.invoke(album.releaseDate),
            genre = album.genre ?: "",
            trackCount = album.trackCount?.toString() ?: "",
            price = buildPriceString(album.price, album.currency)
        )
    }
    
    private fun buildPriceString(price: String?, currency: String?): String {
        return when {
            price != null && currency != null -> "$price $currency"
            price != null -> price
            else -> ""
        }
    }
} 