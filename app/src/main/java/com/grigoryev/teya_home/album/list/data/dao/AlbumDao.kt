package com.grigoryev.teya_home.album.list.data.dao

import androidx.room.*
import com.grigoryev.teya_home.album.list.data.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    
    @Query("SELECT * FROM albums ORDER BY id ASC")
    fun getAllAlbums(): Flow<List<AlbumEntity>>
    
    @Query("SELECT * FROM albums WHERE id = :albumId")
    suspend fun getAlbumById(albumId: Int): AlbumEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)
    
    @Query("DELETE FROM albums WHERE id BETWEEN :startId AND :endId")
    suspend fun deleteAlbumsInRange(startId: Int, endId: Int)
    
    @Delete
    suspend fun deleteAlbum(album: AlbumEntity)
    
    @Query("DELETE FROM albums")
    suspend fun deleteAllAlbums()
} 