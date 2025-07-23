package com.grigoryev.teya_home.album.list.presentation.list

import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseItemModel
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateBaseViewHolder
import com.grigoryev.teya_home.core.util.setSafeOnClickListener
import com.grigoryev.teya_home.databinding.ItemRateMeBinding

class RateMeViewHolder(
    private val listener: DelegateAdapterEventListener,
    override val binding: ItemRateMeBinding,
) : DelegateBaseViewHolder<ItemRateMeBinding>(binding) {

    private val starViews = listOf(
        binding.imageViewStar1,
        binding.imageViewStar2,
        binding.imageViewStar3,
        binding.imageViewStar4,
        binding.imageViewStar5
    )

    override fun bindWith(model: DelegateBaseItemModel) {
        model as RateMeModel

        updateStarStates(model.rating)
        setupStarClickListeners(model)
    }

    private fun setupStarClickListeners(model: RateMeModel) {
        starViews.forEachIndexed { index, starView ->
            starView.setSafeOnClickListener {
                val rating = index + 1
                updateStarStates(rating)
                playStarAnimation(starView)
                listener.invoke(RATING_EVENT_ID, model.listId, rating)
            }
        }
    }

    private fun updateStarStates(rating: Int) {
        starViews.forEachIndexed { index, starView ->
            val isFilled = index < rating
            starView.setImageResource(
                if (isFilled) R.drawable.ic_star_filled else R.drawable.ic_star_outline
            )
        }
    }

    private fun playStarAnimation(starView: ImageView) {
        val animation = AnimationUtils.loadAnimation(starView.context, R.anim.star_scale_animation)
        starView.startAnimation(animation)
    }

    companion object {
        const val RATING_EVENT_ID = "RATING_EVENT_ID"
    }
}

data class RateMeModel(
    override val listId: String = "RATE_ME_DEFAULT_LIST_ID",
    override val payload: Any? = null,
    val rating: Int = 0
) : DelegateBaseItemModel(listId) 