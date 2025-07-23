package com.grigoryev.teya_home.album.list.data

import com.grigoryev.teya_home.album.list.data.dao.AlbumDao
import com.grigoryev.teya_home.album.list.data.entity.AlbumEntity
import com.grigoryev.teya_home.album.list.data.network.ItunesApiService
import com.grigoryev.teya_home.album.list.data.network.ItunesResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AlbumRepository {
    fun getAlbums(): Flow<List<AlbumEntity>>
    suspend fun loadAlbums()
}

class AlbumRepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao,
    private val itunesApiService: ItunesApiService
) : AlbumRepository {
    
    override fun getAlbums(): Flow<List<AlbumEntity>> {
        return albumDao.getAllAlbums()
    }
    

    override suspend fun loadAlbums() {
        val response: ItunesResponse = itunesApiService.getTopAlbums()
        val albums = response.feed.entries.mapIndexed { index, entry ->
            AlbumEntity(
                id = entry.id.attributes.imId.toIntOrNull() ?: index,
                title = entry.name.label,
                artist = entry.artist.label,
                coverUrl = entry.images.lastOrNull()?.label,
                year = entry.releaseDate.attributes.label.toIntOrNull()
            )
        }
        
        albumDao.deleteAllAlbums()
        albumDao.insertAlbums(albums)
    }
}