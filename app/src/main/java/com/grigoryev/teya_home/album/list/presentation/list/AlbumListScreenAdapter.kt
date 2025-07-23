package com.grigoryev.teya_home.album.list.presentation.list

import android.view.ViewGroup
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseViewHolder
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateListAdapter
import com.grigoryev.teya_home.core.util.viewBinding
import com.grigoryev.teya_home.databinding.ItemAlbumBinding
import com.grigoryev.teya_home.databinding.ItemShimmerBinding
import com.grigoryev.teya_home.databinding.ItemRateMeBinding

class AlbumListScreenAdapter(
    private val listener: DelegateAdapterEventListener
) : DelegateListAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DelegateBaseViewHolder<*> {
        return when (viewType) {
            R.layout.item_album -> {
                AlbumItemViewHolder(
                    listener = listener,
                    binding = parent.viewBinding(
                        ItemAlbumBinding::inflate,
                        false
                    )
                )
            }
            R.layout.item_shimmer -> {
                ShimmerItemViewHolder(
                    binding = parent.viewBinding(
                        ItemShimmerBinding::inflate,
                        false
                    )
                )
            }
            R.layout.item_rate_me -> {
                RateMeViewHolder(
                    listener = listener,
                    binding = parent.viewBinding(
                        ItemRateMeBinding::inflate,
                        false
                    )
                )
            }
            else -> error("unknown view type : $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AlbumItemModel -> R.layout.item_album
            is ShimmerItemModel -> R.layout.item_shimmer
            is RateMeModel -> R.layout.item_rate_me
            else -> error("unknown type : ${getItem(position)}")
        }
    }
}