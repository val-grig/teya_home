package com.grigoryev.teya_home.album.list.presentation.list

import com.bumptech.glide.Glide
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemModel
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseViewHolder
import com.grigoryev.teya_home.core.util.setSafeOnClickListener
import com.grigoryev.teya_home.databinding.ItemAlbumBinding
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class AlbumItemViewHolder(
    private val listener: DelegateAdapterEventListener,
    override val binding: ItemAlbumBinding,
) : DelegateBaseViewHolder<ItemAlbumBinding>(binding) {

    override fun bindWith(model: DelegateBaseItemModel) {
        model as AlbumItemModel

        binding.textViewAlbumTitle.text = model.title
        binding.textViewArtist.text = model.subtitle

        val requestOptions = RequestOptions()
            .placeholder(R.drawable.album_placeholder)
            .error(R.drawable.album_placeholder)

        Glide.with(binding.root.context)
            .load(model.imageUrl)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade(CROSSFADE_DURATION_MS))
            .into(binding.imageViewAlbumCover)

        binding.root.setSafeOnClickListener {
            listener.invoke(CLICK_EVENT_ID, model.listId, model.payload)
        }
    }

    companion object {
        const val CLICK_EVENT_ID = "CLICK_EVENT_ID"
        private const val CORNER_RADIUS_DP = 8
        private const val CROSSFADE_DURATION_MS = 300
    }
}

data class AlbumItemModel(
    override val listId: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    override val payload: Any? = null
) : DelegateBaseItemModel(listId)
