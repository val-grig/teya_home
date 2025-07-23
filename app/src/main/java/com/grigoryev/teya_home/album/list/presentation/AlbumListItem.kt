package com.grigoryev.teya_home.album.list.presentation

import com.bumptech.glide.Glide
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemModel
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseViewHolder
import com.grigoryev.teya_home.core.util.setSafeOnClickListener
import com.grigoryev.teya_home.databinding.ItemAlbumBinding

class AlbumItemViewHolder(
    private val listener: DelegateAdapterEventListener,
    override val binding: ItemAlbumBinding,
) : DelegateBaseViewHolder<ItemAlbumBinding>(binding) {

    override fun bindWith(model: DelegateBaseItemModel) {
        model as AlbumItemModel

        binding.textViewAlbumTitle.text = model.title
        binding.textViewArtist.text = model.subtitle

        Glide.with(binding.root.context)
            .load(model.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imageViewAlbumCover)

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
    val imageUrl: String,
    override val payload: Any? = null
) : DelegateBaseItemModel(listId)
