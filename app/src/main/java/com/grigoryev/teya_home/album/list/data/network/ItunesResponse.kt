package com.grigoryev.teya_home.album.list.data.network

import com.google.gson.annotations.SerializedName

data class ItunesResponse(
    @SerializedName("feed")
    val feed: Feed
)

data class Feed(
    @SerializedName("entry")
    val entries: List<Entry>
)

data class Entry(
    @SerializedName("id")
    val id: Id,
    @SerializedName("im:name")
    val name: Label,
    @SerializedName("im:artist")
    val artist: Artist,
    @SerializedName("im:image")
    val images: List<Image>,
    @SerializedName("im:releaseDate")
    val releaseDate: ReleaseDate,
    @SerializedName("im:price")
    val price: Price,
    @SerializedName("im:contentType")
    val contentType: ContentType,
    @SerializedName("category")
    val category: Category
)

data class Id(
    @SerializedName("label")
    val label: String,
    @SerializedName("attributes")
    val attributes: IdAttributes
)

data class IdAttributes(
    @SerializedName("im:id")
    val imId: String
)

data class Label(
    @SerializedName("label")
    val label: String
)

data class Artist(
    @SerializedName("label")
    val label: String
)

data class Image(
    @SerializedName("label")
    val label: String,
    @SerializedName("attributes")
    val attributes: ImageAttributes
)

data class ImageAttributes(
    @SerializedName("height")
    val height: String
)

data class ReleaseDate(
    @SerializedName("label")
    val label: String,
    @SerializedName("attributes")
    val attributes: ReleaseDateAttributes
)

data class ReleaseDateAttributes(
    @SerializedName("label")
    val label: String
)

data class Price(
    @SerializedName("label")
    val label: String,
    @SerializedName("attributes")
    val attributes: PriceAttributes
)

data class PriceAttributes(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("currency")
    val currency: String
)

data class ContentType(
    @SerializedName("label")
    val label: String,
    @SerializedName("attributes")
    val attributes: ContentTypeAttributes
)

data class ContentTypeAttributes(
    @SerializedName("term")
    val term: String,
    @SerializedName("label")
    val label: String
)

data class Category(
    @SerializedName("attributes")
    val attributes: CategoryAttributes
)

data class CategoryAttributes(
    @SerializedName("term")
    val term: String,
    @SerializedName("label")
    val label: String
) 