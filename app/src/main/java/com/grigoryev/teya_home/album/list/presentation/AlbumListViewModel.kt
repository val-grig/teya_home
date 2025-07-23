package com.grigoryev.teya_home.album.list.presentation

import androidx.lifecycle.viewModelScope
import com.grigoryev.teya_home.album.list.domain.GetAlbumsUseCase
import com.grigoryev.teya_home.album.list.domain.LoadAlbumsUseCase
import com.grigoryev.teya_home.album.list.domain.SaveRateUseCase
import com.grigoryev.teya_home.album.list.domain.GetRateUseCase
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.core.mvi.StateViewModel
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemModel
import com.grigoryev.teya_home.core.util.GetRateMessageUtil
import com.grigoryev.teya_home.core.util.launchAndCollectLatestIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.grigoryev.teya_home.core.app.data.ConnectionEvent
import com.grigoryev.teya_home.core.app.data.ConnectionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.grigoryev.teya_home.core.app.domain.GetConnectionUseCase

data class AlbumListModelState(
    val allAlbums: List<AlbumModel> = emptyList(),
    val currentRating: Int = 0,
)

data class AlbumListUIState(
    val listItems: List<DelegateBaseItemModel> = emptyList(),
)

sealed class AlbumListScreenEvent {
    data object ShowError : AlbumListScreenEvent()
    data object HideError : AlbumListScreenEvent()
    data class NavigateToDetails(val albumModel: AlbumModel) : AlbumListScreenEvent()
    data class ShowAlertMessage(val message: String) : AlbumListScreenEvent()
    data class ConnectionStatus(val isConnected: Boolean) : AlbumListScreenEvent()
}

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    mapper: AlbumListScreenMapper,
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val loadAlbumsUseCase: LoadAlbumsUseCase,
    private val saveRateUseCase: SaveRateUseCase,
    private val getRateUseCase: GetRateUseCase,
    private val getConnectionUseCase: GetConnectionUseCase,
    private val getRateMessageUtil: GetRateMessageUtil
) : StateViewModel<AlbumListModelState, AlbumListUIState, AlbumListScreenEvent>(
    initModelState = AlbumListModelState(),
    initScreenState = AlbumListUIState(),
    mapper = mapper
) {

    init {
        loadData()
        observeConnection()
    }

    fun onRetryLoadingPressed() = loadData()

    private fun loadData() = viewModelScope.launch {
        emitScreenEvent(AlbumListScreenEvent.HideError)
        getAlbumsUseCase.invoke().launchAndCollectLatestIn(viewModelScope) { albums ->
            updateModelState { it.copy(allAlbums = albums, currentRating = getRateUseCase.invoke()) }
            mapScreenState()
        }

        runCatching {
            loadAlbumsUseCase.invoke()
        }.onFailure { emitScreenEvent(AlbumListScreenEvent.ShowError) }
    }

    fun onAlbumClicked(model: AlbumModel) = viewModelScope.launch {
        emitScreenEvent(AlbumListScreenEvent.NavigateToDetails(model))
    }

    fun onRatingSubmitted(rating: Int) = viewModelScope.launch {
        saveRateUseCase.invoke(rating)
        updateModelState { it.copy(currentRating = rating) }
        emitScreenEvent(AlbumListScreenEvent.ShowAlertMessage(getRateMessageUtil.getMessage(rating)))
    }

    private fun observeConnection()  {
        getConnectionUseCase.invoke().launchAndCollectLatestIn(viewModelScope) { event ->
            when (event) {
                is ConnectionEvent.ConnectionLost -> {
                    emitScreenEvent(AlbumListScreenEvent.ConnectionStatus(false))
                }

                is ConnectionEvent.ConnectionRecovered -> {
                    if (getModelState().allAlbums.isEmpty()) {
                        loadData()
                    }
                    emitScreenEvent(AlbumListScreenEvent.ConnectionStatus(true))
                }
            }
        }
    }
}