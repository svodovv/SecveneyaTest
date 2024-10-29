package com.example.secveneyatest.presentation.ui.film.list

import com.example.secveneyatest.domain.model.CategoriesModel
import com.example.secveneyatest.domain.model.FilmModel

data class FilmListState (
    val loading: Boolean,
    val error: Boolean,
    val filmList: List<FilmModel>,
    val categoryList: List<CategoriesModel>,
    val selectedCategory: String? = null
){
    companion object{
        val init = FilmListState(
            loading = false,
            error = false,
            filmList = emptyList(),
            categoryList = emptyList(),
            selectedCategory = null
        )
    }
}
