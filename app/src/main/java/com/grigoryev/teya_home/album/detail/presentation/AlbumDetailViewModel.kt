package com.grigoryev.teya_home.album.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.core.mvi.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlbumDetailModelState(
    val album: AlbumModel,
)

data class AlbumDetailUIState(
    val title: String = "",
    val artist: String = "",
    val coverUrl: String = "",
)

sealed class AlbumDetailScreenEvent {
    object ShowError : AlbumDetailScreenEvent()
    data class ShowImageLoadError(val message: String) : AlbumDetailScreenEvent()
}

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : StateViewModel<AlbumDetailModelState, AlbumDetailUIState, AlbumDetailScreenEvent>(
    initModelState = AlbumDetailModelState(savedStateHandle.getNavParams().albumModel),
    initScreenState = AlbumDetailUIState(),
    mapper = AlbumDetailScreenMapper()
) {
    init {
        viewModelScope.launch {
            mapScreenState()
        }
    }
}

private fun SavedStateHandle.getNavParams(): AlbumDetailFragment.NavigationParams {
    return this.get<AlbumDetailFragment.NavigationParams>(AlbumDetailFragment.Companion.NAVIGATION_PARAM_BUNDLE_KEY)
        ?: error("AlbumDetailScreen navigation params in undefined")
}