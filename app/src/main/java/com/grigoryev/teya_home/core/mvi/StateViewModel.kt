package com.grigoryev.teya_home.core.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.withContext

abstract class StateViewModel<ModelState, UIState, ScreenEvent>(
    initModelState: ModelState,
    initScreenState: UIState,
    screenCache: ModelState? = null,
    private val mapper: StateScreenMapper<ModelState, UIState>?
) : ViewModel() {
    private val bufferedEvents = mutableListOf<ScreenEvent>()
    private var modelState: ModelState = screenCache ?: initModelState
    private val uiStateStream: MutableStateFlow<UIState> = MutableStateFlow(
        screenCache?.let { mapper?.map(it) } ?: initScreenState
    )
    private val eventStream: MutableSharedFlow<ScreenEvent> = MutableSharedFlow(
        replay = 0,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    fun getScreenState(): StateFlow<UIState> = uiStateStream

    fun getEvent(): Flow<ScreenEvent> = eventStream.onSubscription {
        bufferedEvents.forEach { bufferedScreenEvent ->
            this.emit(bufferedScreenEvent)
        }
        bufferedEvents.clear()
    }

    protected fun getModelState(): ModelState = modelState

    protected fun updateModelState(copyFunction: (ModelState) -> ModelState) {
        modelState = copyFunction.invoke(modelState)
    }

    protected suspend fun mapScreenState(
        onCompleteFunction: (suspend () -> Unit)? = null
    ) = withContext(Dispatchers.Default) {
        val uiState = mapper?.map(modelState) ?: return@withContext

        withContext(Dispatchers.Main) {
            uiStateStream.emit(uiState)
            onCompleteFunction?.invoke()
        }
    }

    protected suspend fun emitScreenEvent(event: ScreenEvent, emitComplete: (suspend () -> Unit)? = null) =
        withContext(Dispatchers.Main) {
            if (eventStream.subscriptionCount.value == 0) {
                bufferedEvents.add(event)
            } else {
                eventStream.emit(event)
            }

            emitComplete?.invoke()
        }
}