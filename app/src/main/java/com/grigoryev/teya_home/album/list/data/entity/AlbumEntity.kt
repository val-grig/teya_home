package com.grigoryev.teya_home.album.list.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val artist: String,
    val coverUrl: String?,
    val releaseDate: String?,
    val genre: String?,
    val trackCount: Int?,
    val price: String?,
    val currency: String?
)