package com.grigoryev.teya_home.album.detail.presentation

import com.grigoryev.teya_home.core.mvi.StateScreenMapper

class AlbumDetailScreenMapper : StateScreenMapper<AlbumDetailModelState, AlbumDetailUIState>() {
    override fun map(modelState: AlbumDetailModelState): AlbumDetailUIState {
        return AlbumDetailUIState(
            title = modelState.album.title,
            artist = modelState.album.artist,
            coverUrl = modelState.album.coverUrl,
        )
    }
} 