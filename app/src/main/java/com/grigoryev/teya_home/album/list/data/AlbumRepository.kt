package com.grigoryev.teya_home.album.list.data

import com.grigoryev.teya_home.album.list.data.dao.AlbumDao
import com.grigoryev.teya_home.album.list.data.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AlbumRepository {
    fun getAllAlbums(): Flow<List<AlbumEntity>>
    suspend fun getAlbumById(id: Int): AlbumEntity?
    suspend fun insertAlbum(album: AlbumEntity)
    suspend fun insertAlbums(albums: List<AlbumEntity>)
    suspend fun deleteAlbum(album: AlbumEntity)
    suspend fun deleteAllAlbums()
}

class AlbumRepositoryImpl @Inject constructor(
    private val albumDao: AlbumDao
) : AlbumRepository {
    
    override fun getAllAlbums(): Flow<List<AlbumEntity>> {
        return albumDao.getAllAlbums()
    }
    
    override suspend fun getAlbumById(id: Int): AlbumEntity? {
        return albumDao.getAlbumById(id)
    }
    
    override suspend fun insertAlbum(album: AlbumEntity) {
        albumDao.insertAlbum(album)
    }
    
    override suspend fun insertAlbums(albums: List<AlbumEntity>) {
        albumDao.insertAlbums(albums)
    }
    
    override suspend fun deleteAlbum(album: AlbumEntity) {
        albumDao.deleteAlbum(album)
    }
    
    override suspend fun deleteAllAlbums() {
        albumDao.deleteAllAlbums()
    }
}