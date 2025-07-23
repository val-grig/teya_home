package com.grigoryev.teya_home.album.list.presentation

import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.album.list.presentation.list.AlbumItemModel
import com.grigoryev.teya_home.album.list.presentation.list.RateMeModel
import com.grigoryev.teya_home.album.list.presentation.list.ShimmerItemModel
import com.grigoryev.teya_home.core.mvi.StateScreenMapper
import javax.inject.Inject

class AlbumListScreenMapper @Inject constructor() : StateScreenMapper<AlbumListModelState, AlbumListUIState>() {

    override fun map(modelState: AlbumListModelState): AlbumListUIState {
        if (modelState.allAlbums.isEmpty()) {
            return getLoadingState()
        }

        return AlbumListUIState(
            listItems = buildList {
                addAll(modelState.allAlbums.mapToPresentation())
                add(RateMeModel(rating = modelState.currentRating))
            }
        )
    }

    private fun getLoadingState(): AlbumListUIState {
        return AlbumListUIState(
            listItems = List(SHIMMER_ITEM_COUNT) { index ->
                ShimmerItemModel
            }
        )
    }

    private fun List<AlbumModel>.mapToPresentation(): List<AlbumItemModel> {
        return map { albumModel ->
            AlbumItemModel(
                listId = albumModel.id.toString(),
                title = albumModel.title,
                subtitle = albumModel.artist,
                imageUrl = albumModel.coverUrl,
                payload = albumModel
            )
        }
    }

    companion object {
        private const val SHIMMER_ITEM_COUNT = 5
    }
}