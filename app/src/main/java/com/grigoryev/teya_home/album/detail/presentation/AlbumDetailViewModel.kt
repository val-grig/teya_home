package com.grigoryev.teya_home.album.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.core.app.data.ConnectionEvent
import com.grigoryev.teya_home.core.app.domain.GetConnectionUseCase
import com.grigoryev.teya_home.core.mvi.StateViewModel
import com.grigoryev.teya_home.core.util.launchAndCollectLatestIn
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
    val releaseDate: String = "",
    val genre: String = "",
    val trackCount: String = "",
    val price: String = ""
)

sealed class AlbumDetailScreenEvent {
    object ShowError : AlbumDetailScreenEvent()
    data class ShowImageLoadError(val message: String) : AlbumDetailScreenEvent()
    data class ConnectionStatus(val isConnected: Boolean) : AlbumDetailScreenEvent()
}

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mapper: AlbumDetailScreenMapper,
    private val getConnectionUseCase: GetConnectionUseCase
) : StateViewModel<AlbumDetailModelState, AlbumDetailUIState, AlbumDetailScreenEvent>(
    initModelState = AlbumDetailModelState(savedStateHandle.getNavParams().albumModel),
    initScreenState = AlbumDetailUIState(),
    mapper = mapper
) {
    init {
        viewModelScope.launch {
            mapScreenState()
        }
        observeConnection()
    }
    
    private fun observeConnection() {
        getConnectionUseCase.invoke().launchAndCollectLatestIn(viewModelScope) { event ->
            when (event) {
                is ConnectionEvent.ConnectionLost -> {
                    emitScreenEvent(AlbumDetailScreenEvent.ConnectionStatus(false))
                }
                is ConnectionEvent.ConnectionRecovered -> {
                    emitScreenEvent(AlbumDetailScreenEvent.ConnectionStatus(true))
                }
            }
        }
    }
}

private fun SavedStateHandle.getNavParams(): AlbumDetailFragment.NavigationParams {
    return this.get<AlbumDetailFragment.NavigationParams>(AlbumDetailFragment.Companion.NAVIGATION_PARAM_BUNDLE_KEY) ?: throw IllegalArgumentException("Album model is required")
}