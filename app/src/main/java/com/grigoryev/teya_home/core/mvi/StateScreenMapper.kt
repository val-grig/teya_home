package com.grigoryev.teya_home.core.mvi

internal abstract class StateScreenMapper<ModelState, ScreenState> {
    abstract fun map(modelState: ModelState): ScreenState
}