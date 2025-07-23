package com.grigoryev.teya_home.album.list.presentation

import com.grigoryev.teya_home.core.mvi.StateScreenMapper

class AlbumListScreenMapper : StateScreenMapper<AlbumListModelState, AlbumListUIState>() {
    override fun map(modelState: AlbumListModelState): AlbumListUIState {
        return AlbumListUIState(
            listItems = emptyList(),
            showInitialLoading = false
        )
    }
}