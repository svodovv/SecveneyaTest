package com.example.secveneyatest.domain.repository

import com.example.secveneyatest.data.remote.dto.FilmsDto
import com.example.secveneyatest.domain.model.CategoriesModel
import com.example.secveneyatest.domain.model.FilmDetailModel
import com.example.secveneyatest.domain.model.FilmModel
import com.example.secveneyatest.utils.Resource
import kotlinx.coroutines.flow.Flow

interface FilmRepository {
    suspend fun getFilms(): Flow<Resource<List<FilmModel>>>
    suspend fun getCategories(): Flow<Resource<List<CategoriesModel>>>
    suspend fun getFilmsByCategories(categoryName: String?): List<FilmModel>
    suspend fun getFilmById(filmId: Int): FilmDetailModel?
}
