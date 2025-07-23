package com.grigoryev.teya_home.album.list.presentation

import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemModel
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemPayload
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseViewHolder
import com.grigoryev.teya_home.core.util.setSafeOnClickListener
import com.grigoryev.teya_home.databinding.ItemAlbumBinding

class AlbumItemViewHolder(
    private val listener: DelegateAdapterEventListener,
    override val binding: ItemAlbumBinding,
) : DelegateBaseViewHolder<ItemAlbumBinding>(binding) {

    override fun bindWith(model: DelegateBaseItemModel) {
        model as AlbumItemModel

        binding.root.setSafeOnClickListener {
            listener.invoke(CLICK_EVENT_ID, model.listId, model.payload)
        }
    }

    companion object {
        const val CLICK_EVENT_ID = "CLICK_EVENT_ID"
    }
}

data class AlbumItemModel(
    override val listId: String,
    val title: String,
    val subtitle: String,
    override val payload: Any? = null
) : DelegateBaseItemModel(listId)
