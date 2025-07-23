package com.grigoryev.teya_home.core.mvi.delegate_adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.grigoryev.teya_home.core.util.dpToPx

abstract class DelegateBaseViewHolder<VB>(protected open val binding: VB) :
    RecyclerView.ViewHolder(binding.root) where VB : ViewBinding {
    abstract fun bindWith(model: DelegateBaseItemModel)
    open fun onDetached() = Unit
    open fun onAttached() = Unit

    protected fun setMargin(margin: DelegateBaseItemModel.Margin) {
        val layoutParams = binding.root.layoutParams as? ViewGroup.MarginLayoutParams ?: return
        val left = margin.leftDp.dpToPx()
        val top = margin.topDp.dpToPx()
        val right = margin.rightDp.dpToPx()
        val bottom = margin.bottomDp.dpToPx()

        if (left != layoutParams.marginStart ||
            right != layoutParams.marginEnd ||
            left != layoutParams.leftMargin ||
            right != layoutParams.rightMargin ||
            top != layoutParams.topMargin ||
            bottom != layoutParams.bottomMargin
        ) {
            layoutParams.marginStart = margin.leftDp.dpToPx()
            layoutParams.marginEnd = margin.rightDp.dpToPx()

            layoutParams.setMargins(
                margin.leftDp.dpToPx(),
                margin.topDp.dpToPx(),
                margin.rightDp.dpToPx(),
                margin.bottomDp.dpToPx()
            )

            binding.root.layoutParams = layoutParams
        }
    }

    open fun bindWithPayload(payload: DelegateBaseItemPayload) = Unit
}