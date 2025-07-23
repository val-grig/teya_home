package com.grigoryev.teya_home.album.list.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.album.detail.presentation.AlbumDetailFragment
import com.grigoryev.teya_home.album.detail.presentation.AlbumDetailFragment.*
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import com.grigoryev.teya_home.album.list.presentation.list.AlbumItemViewHolder
import com.grigoryev.teya_home.album.list.presentation.list.AlbumListScreenAdapter
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.util.applyStatusBarPadding
import com.grigoryev.teya_home.core.util.launchAndCollectLatestIn
import com.grigoryev.teya_home.core.util.setSlideAnimType
import com.grigoryev.teya_home.core.util.viewBinding
import com.grigoryev.teya_home.databinding.FragmentAlbumListBinding
import com.grigoryev.teya_home.databinding.ItemRateMeBinding
import com.grigoryev.teya_home.album.list.presentation.list.RateMeViewHolder
import com.grigoryev.teya_home.album.list.presentation.list.RateMeModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AlbumListScreenFragment : Fragment(R.layout.fragment_album_list) {
    private val binding by viewBinding(FragmentAlbumListBinding::bind)
    private val viewModel by viewModels<AlbumListViewModel>()
    private var snackbar: Snackbar? = null

    private val screenAdapter = AlbumListScreenAdapter(object : DelegateAdapterEventListener {
        override fun invoke(eventId: String, listId: String, payload: Any?) {
            when (eventId) {
                AlbumItemViewHolder.CLICK_EVENT_ID -> {
                    val model = payload as? AlbumModel ?: return
                    viewModel.onAlbumClicked(model)
                }
                RateMeViewHolder.RATING_EVENT_ID -> {
                    val rating = payload as? Int ?: return
                    viewModel.onRatingSubmitted(rating)
                }
                else -> error("unknown event : ${eventId}")
            }
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyStatusBarPadding(additionalTopPaddingDp = 12)

        setupRecyclerView()
        listenScreenModel()
    }

    private fun listenScreenModel() {
        viewModel.getScreenState()
            .launchAndCollectLatestIn(viewLifecycleOwner) { state -> onState(state) }
        viewModel.getEvent()
            .launchAndCollectLatestIn(viewLifecycleOwner) { event -> onEvent(event) }
    }

    private fun onState(state: AlbumListUIState) {
        screenAdapter.submitList(state.listItems)
    }

    private fun onEvent(event: AlbumListScreenEvent) {
        when (event) {
            is AlbumListScreenEvent.NavigateToDetails -> {
                val detailFragment = AlbumDetailFragment.newInstance(NavigationParams(event.albumModel))
                parentFragmentManager.commit {
                    setSlideAnimType()
                    replace(R.id.fragmentContainer, detailFragment)
                    addToBackStack(null)
                }
            }

            is AlbumListScreenEvent.ShowError -> {
                showErrorSnackbar()
            }

            is AlbumListScreenEvent.HideError -> {
                hideErrorSnackbar()
            }
            
            is AlbumListScreenEvent.ShowAlertMessage -> {
                showAlertMessage(event.message)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAlbums.adapter = screenAdapter
        binding.recyclerViewAlbums.itemAnimator = DefaultItemAnimator().apply {
            supportsChangeAnimations = false
        }
    }

    private fun showErrorSnackbar() {
        hideErrorSnackbar()

        snackbar = Snackbar.make(
            binding.root,
            R.string.loading_failed_message,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.retry) {
            viewModel.onRetryLoadingPressed()
        }

        snackbar?.show()
    }

    private fun hideErrorSnackbar() {
        snackbar?.dismiss()
        snackbar = null
    }
    
    private fun showAlertMessage(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}