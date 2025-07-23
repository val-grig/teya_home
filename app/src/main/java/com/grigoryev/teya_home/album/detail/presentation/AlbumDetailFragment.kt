package com.grigoryev.teya_home.album.detail.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.core.util.launchAndCollectLatestIn
import com.grigoryev.teya_home.core.util.viewBinding
import com.grigoryev.teya_home.databinding.FragmentAlbumDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

@AndroidEntryPoint
class AlbumDetailFragment : Fragment(R.layout.fragment_album_detail) {
    private val binding by viewBinding(FragmentAlbumDetailBinding::bind)
    private val viewModel by viewModels<AlbumDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackButton()
        listenScreenModel()
    }

    private fun initBackButton() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun listenScreenModel() {
        viewModel.getScreenState()
            .launchAndCollectLatestIn(viewLifecycleOwner) { state -> onState(state) }
        viewModel.getEvent()
            .launchAndCollectLatestIn(viewLifecycleOwner) { event -> onEvent(event) }
    }

    private fun onState(state: AlbumDetailUIState) {
        binding.textViewAlbumTitle.text = state.title
        binding.textViewArtist.text = state.artist

        if (state.coverUrl.isNotEmpty()) {
            loadImage(state.coverUrl)
        }

        binding.textViewReleaseDate.text = if (state.releaseDate.isNotEmpty()) {
            getString(R.string.album_detail_released_template, state.releaseDate)
        } else ""
        
        binding.textViewGenre.text = if (state.genre.isNotEmpty()) {
            getString(R.string.album_detail_genre_template, state.genre)
        } else ""
        
        binding.textViewTrackCount.text = if (state.trackCount.isNotEmpty()) {
            getString(R.string.album_detail_tracks_template, state.trackCount)
        } else ""
        
        binding.textViewPrice.text = state.price
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imageViewAlbumCover)
    }

    private fun onEvent(event: AlbumDetailScreenEvent) {
        when (event) {
            is AlbumDetailScreenEvent.ShowError -> Unit
            is AlbumDetailScreenEvent.ShowImageLoadError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Parcelize
    data class NavigationParams(val albumModel: AlbumModel) : Parcelable

    companion object {
        const val NAVIGATION_PARAM_BUNDLE_KEY = "navigation_params"

        fun newInstance(params: NavigationParams): AlbumDetailFragment {
            return AlbumDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(NAVIGATION_PARAM_BUNDLE_KEY, params)
                }
            }
        }
    }
} 