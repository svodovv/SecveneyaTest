package com.example.secveneyatest.data.remote.dto

import com.example.secveneyatest.domain.model.CategoriesModel
import java.util.Locale.Category

data class FilmsDto(
    val films: List<FilmDto>
){
    fun toCategoryListModel(): List<CategoriesModel>
        = films.flatMap { it.genres }.toSet().map { CategoriesModel(it) }

}