package com.example.secveneyatest.data.repository

import com.example.secveneyatest.data.remote.dto.FilmsDto
import com.example.secveneyatest.data.remote.dto.toFilmDetailModel
import com.example.secveneyatest.data.remote.dto.toFilmModel
import com.example.secveneyatest.data.remote.retrofit.FilmsApi
import com.example.secveneyatest.domain.model.CategoriesModel
import com.example.secveneyatest.domain.model.FilmDetailModel
import com.example.secveneyatest.domain.model.FilmModel
import com.example.secveneyatest.domain.repository.FilmRepository
import com.example.secveneyatest.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale


class FilmRepositoryImpl(
    private val filmsApi: FilmsApi
) : FilmRepository {

    private var filmList: FilmsDto? = null
    private val mutex = Mutex()


    override suspend fun getFilms(): Flow<Resource<List<FilmModel>>> = flow {
        when (val resource = loadFilms()) {
            is Resource.Error -> emit(Resource.Error("Error in repo"))
            is Resource.Success -> {
                val filmModelList =
                    resource.data?.films
                        ?.map { it.toFilmModel() }
                        ?.sortedBy { it.name.first() }

                filmModelList?.let {
                    emit(Resource.Success(filmModelList))
                }

            }
        }
    }

    override suspend fun getFilmsByCategories(categoryName: String?): List<FilmModel> {
        if (categoryName == null)
            return filmList?.films?.map { it.toFilmModel() } ?: emptyList()

        return filmList?.films
            ?.filter {
                val categoryNameToLoverCase = categoryName.lowercase(Locale.ROOT)
                it.genres.contains(categoryNameToLoverCase)
            }
            ?.map { it.toFilmModel() }
            ?: emptyList()

    }

    override suspend fun getCategories(): Flow<Resource<List<CategoriesModel>>> = flow {
        when (val resource = loadFilms()) {
            is Resource.Error -> emit(Resource.Error("Error in repo"))
            is Resource.Success -> {
                resource.data?.toCategoryListModel()?.let { listCategoryModel ->

                    val firstUpperCaseCategoryList = listCategoryModel
                        .map { CategoriesModel(it.category.replaceFirstChar { it.uppercaseChar() })}
                        .sortedBy { it.category.first() }

                    emit(Resource.Success(firstUpperCaseCategoryList))
                }
            }
        }
    }

    override suspend fun getFilmById(filmId: Int) =
        filmList?.films?.first { it.id == filmId }?.toFilmDetailModel()


    private suspend fun loadFilms(): Resource<FilmsDto> {
        mutex.withLock {
            return try {
                if (!filmList?.films.isNullOrEmpty()) {
                    Resource.Success(filmList!!)
                } else {
                    filmList = filmsApi.getFilms()
                    filmList?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Film list is null")
                }
            } catch (e: HttpException) {
                Resource.Error("Сетевая ошибка: ${e.message()}")
            } catch (e: IOException) {
                Resource.Error("Ошибка подключения. Проверьте интернет-соединение.")
            } catch (e: Exception) {
                Resource.Error("Неизвестная ошибка: ${e.localizedMessage}")
            }
        }
    }



}
