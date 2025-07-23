package com.grigoryev.teya_home.album.list.presentation

import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.core.mvi.StateScreenMapper

class AlbumListScreenMapper : StateScreenMapper<AlbumListModelState, AlbumListUIState>() {
    override fun map(modelState: AlbumListModelState): AlbumListUIState {
        return AlbumListUIState(
            listItems = modelState.allAlbums.mapToPresentation(),
            showInitialLoading = false
        )
    }

    private fun List<AlbumModel>.mapToPresentation(): List<AlbumItemModel> {
        return map { albumModel ->
            AlbumItemModel(
                listId = albumModel.id.toString(),
                title = albumModel.title,
                subtitle = albumModel.artist
            )
        }
    }
}