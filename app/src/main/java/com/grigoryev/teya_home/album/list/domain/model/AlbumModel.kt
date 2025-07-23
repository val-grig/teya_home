package com.grigoryev.teya_home.album.list.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumModel(
    val id: Int,
    val title: String,
    val artist: String,
    val coverUrl: String,
    val releaseDate: String?,
    val genre: String?,
    val trackCount: Int?,
    val price: String?,
    val currency: String?
) : Parcelable