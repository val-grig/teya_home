package com.grigoryev.teya_home.album.list.data.network

import retrofit2.http.GET

interface ItunesApiService {
    @GET("us/rss/topalbums/limit=100/json")
    suspend fun getTopAlbums(): ItunesResponse
} 