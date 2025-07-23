package com.grigoryev.teya_home.album.list.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.grigoryev.teya_home.R
import com.grigoryev.teya_home.core.mvi.delegate_adapter.DelegateAdapterEventListener
import com.grigoryev.teya_home.core.util.viewBinding
import com.grigoryev.teya_home.databinding.FragmentAlbumListBinding

class AlbumListScreenFragment : Fragment(R.layout.fragment_album_list) {
    private val binding by viewBinding(FragmentAlbumListBinding::bind)

    private val screenAdapter = AlbumListScreenAdapter(object : DelegateAdapterEventListener {
        override fun invoke(eventId: String, listId: String, payload: Any?) {
            TODO("Not yet implemented")
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        showAlbums()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAlbums.adapter = screenAdapter
    }

    private fun showAlbums() {
        val sampleAlbums = listOf(
            AlbumItemModel("1", "The Dark Side of the Moon", "Pink Floyd"),
            AlbumItemModel("2", "Abbey Road", "The Beatles"),
            AlbumItemModel("3", "Thriller", "Michael Jackson"),
            AlbumItemModel("4", "Back in Black", "AC/DC"),
            AlbumItemModel("5", "The Wall", "Pink Floyd")
        )

        screenAdapter.submitList(sampleAlbums)
    }

    private fun showEmptyState(show: Boolean) {
        binding.textViewEmpty.visibility = if (show) View.VISIBLE else View.GONE
    }
}