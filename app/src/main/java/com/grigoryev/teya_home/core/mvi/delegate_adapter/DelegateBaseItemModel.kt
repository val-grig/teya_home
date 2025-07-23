package com.grigoryev.teya_home.core.mvi.delegate_adapter

abstract class DelegateBaseItemModel(
    open val listId: String,
    open val payload: Any? = null,
    open val margin: Margin = Margin.Default
) {
    data class Margin(val leftDp: Int = 16, val topDp: Int = 16, val rightDp: Int = 16, val bottomDp: Int = 16) {
        companion object {
            val Equal = Margin(
                leftDp = 16,
                topDp = 16,
                rightDp = 16,
                bottomDp = 16
            )
            val Default = Margin(
                leftDp = 16,
                topDp = 0,
                rightDp = 16,
                bottomDp = 16
            )
            val Blank = Margin(
                leftDp = 0,
                topDp = 0,
                rightDp = 0,
                bottomDp = 0
            )
        }
    }
}