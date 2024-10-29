package com.example.secveneyatest.presentation.ui.film.list.recycler

import com.example.secveneyatest.domain.model.FilmModel

sealed class FilmItem {
    data class Category(val name: String): FilmItem()
    data class Film(val film: FilmModel): FilmItem()
    data class Header(val name: String): FilmItem()
}
