package com.grigoryev.teya_home.core.mvi

abstract class StateScreenMapper<ModelState, ScreenState> {
    abstract fun map(modelState: ModelState): ScreenState
}