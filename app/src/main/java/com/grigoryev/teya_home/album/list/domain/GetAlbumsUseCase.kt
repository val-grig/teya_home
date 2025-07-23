package com.grigoryev.teya_home.album.list.domain

import com.grigoryev.teya_home.album.list.data.AlbumRepository
import com.grigoryev.teya_home.album.list.data.AlbumRepositoryImpl
import com.grigoryev.teya_home.album.list.data.entity.AlbumEntity
import com.grigoryev.teya_home.album.list.domain.model.AlbumModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(private val repository: AlbumRepository) {
    fun invoke(): Flow<List<AlbumModel>> {
        return repository.getAlbums().map { albumEntity ->
            albumEntity.mapToDomain()
        }
    }

    private fun List<AlbumEntity>.mapToDomain(): List<AlbumModel> {
        return this.mapNotNull { albumEntity ->
            AlbumModel(
                id = albumEntity.id,
                title = albumEntity.title,
                artist = albumEntity.artist,
                coverUrl = albumEntity.coverUrl ?: return@mapNotNull null,
            )
        }
    }
}