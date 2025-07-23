package com.grigoryev.teya_home.album.list.presentation

import androidx.lifecycle.viewModelScope
import com.grigoryev.teya_home.album.list.domain.GetAlbumsUseCase
import com.grigoryev.teya_home.album.list.domain.LoadAlbumsUseCase
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.core.mvi.StateViewModel
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemModel
import com.grigoryev.teya_home.core.util.launchAndCollectLatestIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlbumListModelState(
    val allAlbums: List<AlbumModel> = emptyList(),
)

data class AlbumListUIState(
    val listItems: List<DelegateBaseItemModel> = emptyList(),
    val showInitialLoading: Boolean = true
)

sealed class AlbumListScreenEvent {
    object ShowError : AlbumListScreenEvent()
    data class NavigateToDetails(val albumModel: AlbumModel) : AlbumListScreenEvent()
}

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val loadAlbumsUseCase: LoadAlbumsUseCase
) : StateViewModel<AlbumListModelState, AlbumListUIState, AlbumListScreenEvent>(
    initModelState = AlbumListModelState(),
    initScreenState = AlbumListUIState(),
    mapper = AlbumListScreenMapper()
) {

    init {
        loadAlbums()
    }

    private fun loadAlbums() = viewModelScope.launch {
        getAlbumsUseCase.invoke().launchAndCollectLatestIn(viewModelScope) { albums ->
            updateModelState { it.copy(allAlbums = albums) }
            mapScreenState()
        }

        runCatching {
            loadAlbumsUseCase.invoke()
        }.onFailure { showError(it) }
    }

    fun onAlbumClicked(model: AlbumModel) = viewModelScope.launch {
        emitScreenEvent(AlbumListScreenEvent.NavigateToDetails(model))
    }

    private fun showError(error: Throwable) {

    }
}