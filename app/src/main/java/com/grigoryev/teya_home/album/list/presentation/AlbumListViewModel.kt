package com.grigoryev.teya_home.album.list.presentation

import androidx.lifecycle.viewModelScope
import com.grigoryev.teya_home.core.mvi.StateViewModel
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemModel
import kotlinx.coroutines.launch

data class AlbumListModelState(
    val albums: List<AlbumItemModel> = emptyList(),
)

data class AlbumListUIState(
    val listItems: List<DelegateBaseItemModel> = emptyList(),
    val showInitialLoading: Boolean = true
)

sealed class AlbumListScreenEvent {
    object ShowError : AlbumListScreenEvent()
}

class AlbumListViewModel : StateViewModel<AlbumListModelState, AlbumListUIState, AlbumListScreenEvent>(
    initModelState = AlbumListModelState(),
    initScreenState = AlbumListUIState(),
    mapper = AlbumListScreenMapper()
) {

    init {
        viewModelScope.launch {
            loadAlbums()
        }
    }

    private suspend fun loadAlbums() {
        val sampleAlbums = listOf(
            AlbumItemModel("1", "The Dark Side of the Moon", "Pink Floyd"),
            AlbumItemModel("2", "Abbey Road", "The Beatles"),
            AlbumItemModel("3", "Thriller", "Michael Jackson"),
            AlbumItemModel("4", "Back in Black", "AC/DC"),
            AlbumItemModel("5", "The Wall", "Pink Floyd")
        )

        updateModelState { it.copy(albums = sampleAlbums) }
        mapScreenState()
    }

    fun onAlbumClicked(model: AlbumItemModel) = viewModelScope.launch {

    }
}