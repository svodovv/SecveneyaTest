package com.example.secveneyatest.domain.model

import com.google.gson.annotations.SerializedName

data class FilmDetailModel (
    val name: String,
    val description: String?,
    val genres: List<String>,
    val imageUrl: String?,
    val localizedName: String,
    val rating: Double?,
    val year: Int
)