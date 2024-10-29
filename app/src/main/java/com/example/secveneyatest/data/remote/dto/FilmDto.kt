package com.example.secveneyatest.data.remote.dto

import com.example.secveneyatest.domain.model.CategoriesModel
import com.example.secveneyatest.domain.model.FilmDetailModel
import com.example.secveneyatest.domain.model.FilmModel
import com.google.gson.annotations.SerializedName

data class FilmDto(
    val description: String?,
    val genres: List<String>,
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("localized_name")
    val localizedName: String,
    val name: String,
    val rating: Double?,
    val year: Int
)

fun FilmDto.toFilmModel(): FilmModel {
    return FilmModel(
        id = id,
        imageUrl = imageUrl,
        name = localizedName
    )
}

fun FilmDto.toFilmDetailModel(): FilmDetailModel{
    return FilmDetailModel(
        name = name,
        description = description,
        genres = genres,
        imageUrl = imageUrl,
        localizedName = localizedName,
        rating = rating,
        year =year
    )
}

