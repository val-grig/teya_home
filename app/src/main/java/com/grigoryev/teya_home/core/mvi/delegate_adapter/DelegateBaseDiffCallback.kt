package com.grigoryev.teya_home.core.mvi.delegate_adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

internal open class DelegateBaseDiffCallback : DiffUtil.ItemCallback<DelegateBaseItemModel>() {

    override fun areItemsTheSame(oldItem: DelegateBaseItemModel, newItem: DelegateBaseItemModel): Boolean {
        return oldItem.listId == newItem.listId
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DelegateBaseItemModel, newItem: DelegateBaseItemModel): Boolean {
        return oldItem == newItem
    }
}