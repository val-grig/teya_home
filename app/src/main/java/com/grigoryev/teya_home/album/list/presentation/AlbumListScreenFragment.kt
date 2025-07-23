package com.grigoryev.teya_home.album.list.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.util.launchAndCollectLatestIn
import com.grigoryev.teya_home.core.util.viewBinding
import com.grigoryev.teya_home.databinding.FragmentAlbumListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class AlbumListScreenFragment : Fragment(R.layout.fragment_album_list) {
    private val binding by viewBinding(FragmentAlbumListBinding::bind)
    private val viewModel by viewModels<AlbumListViewModel>()

    private val screenAdapter = AlbumListScreenAdapter(object : DelegateAdapterEventListener {
        override fun invoke(eventId: String, listId: String, payload: Any?) {
            TODO("Not yet implemented")
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    }

    private fun setupRecyclerView() {
        binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAlbums.adapter = screenAdapter
    }
}