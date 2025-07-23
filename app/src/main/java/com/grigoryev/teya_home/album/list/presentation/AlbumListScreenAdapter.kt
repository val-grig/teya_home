package com.grigoryev.teya_home.album.list.presentation

import android.view.ViewGroup
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseViewHolder
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateListAdapter
import com.grigoryev.teya_home.core.util.viewBinding
import com.grigoryev.teya_home.databinding.ItemAlbumBinding

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

            else -> error("unknown view type : $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AlbumItemModel -> R.layout.item_album
            else -> error("unknown type : ${getItem(position)}")
        }
    }

}