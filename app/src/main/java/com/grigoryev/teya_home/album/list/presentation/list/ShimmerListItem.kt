package com.grigoryev.teya_home.album.list.presentation.list

import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemModel
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseViewHolder
import com.grigoryev.teya_home.databinding.ItemShimmerBinding

class ShimmerItemViewHolder(
    override val binding: ItemShimmerBinding,
) : DelegateBaseViewHolder<ItemShimmerBinding>(binding) {

    override fun bindWith(model: DelegateBaseItemModel) {
        model as ShimmerItemModel
        binding.shimmerLayout.startShimmer()
    }

    override fun onAttached() {
        super.onAttached()
        binding.shimmerLayout.startShimmer()
    }

    override fun onDetached() {
        super.onDetached()
        binding.shimmerLayout.stopShimmer()
    }
}

data object ShimmerItemModel : DelegateBaseItemModel("default_shimmer")