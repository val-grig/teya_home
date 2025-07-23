package com.grigoryev.teya_home.album.list.domain

import com.grigoryev.teya_home.album.list.data.AlbumRepository
import com.grigoryev.teya_home.album.list.data.AlbumRepositoryImpl
import com.grigoryev.teya_home.album.list.data.entity.AlbumEntity
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

class LoadAlbumsUseCase @Inject constructor(private val repository: AlbumRepository) {
    suspend fun invoke() = withContext(Dispatchers.IO) {
        repository.loadAlbums()
    }
}