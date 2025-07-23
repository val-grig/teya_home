package com.grigoryev.teya_home.core.mvi.delegate_adapter

interface DelegateAdapterEventListener {
    fun invoke(eventId: String, listId: String, payload: Any? = null)
}