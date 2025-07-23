package com.grigoryev.teya_home.core.mvi.delegate_adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class DelegateListAdapter(diffUtil: DiffUtil.ItemCallback<DelegateBaseItemModel> = DelegateBaseDiffCallback()) :
    ListAdapter<DelegateBaseItemModel, DelegateBaseViewHolder<*>>(diffUtil) {

    fun emitPayload(listId: String, payload: DelegateBaseItemPayload) {
        val updateIndex = currentList.indexOfFirst { it.listId == listId }
        notifyItemChanged(updateIndex, payload)
    }

    override fun onBindViewHolder(holder: DelegateBaseViewHolder<*>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            onBindPayload(holder, payloads)
        }
    }

    override fun onBindViewHolder(holder: DelegateBaseViewHolder<*>, position: Int) {
        getItem(position)?.let(holder::bindWith)
    }

    override fun onViewDetachedFromWindow(holder: DelegateBaseViewHolder<*>) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetached()
    }

    override fun onViewAttachedToWindow(holder: DelegateBaseViewHolder<*>) {
        super.onViewAttachedToWindow(holder)
        holder.onAttached()
    }

    private fun onBindPayload(
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        if (holder is DelegateBaseViewHolder<*>) {
            payloads.forEach { payload ->
                if (payload is DelegateBaseItemPayload) {
                    holder.bindWithPayload(payload)
                }
            }
        }
    }
}