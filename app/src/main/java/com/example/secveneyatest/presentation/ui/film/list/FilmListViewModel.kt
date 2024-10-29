package com.example.secveneyatest.presentation.ui.film.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secveneyatest.domain.repository.FilmRepository
import com.example.secveneyatest.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilmListViewModel(
    private val filmRepository: FilmRepository
) : ViewModel() {

    private val _state: MutableStateFlow<FilmListState> = MutableStateFlow(FilmListState.init)
    val state = _state.asStateFlow()


    init {
        loadFilms()
    }

    fun loadFilms() {

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(loading = true) }

            combine(
                filmRepository.getFilms(),
                filmRepository.getCategories()
            ) { films, categories ->
                when {
                    films is Resource.Error -> {
                        _state.update { it.copy(error = true, loading = false) }
                    }

                    categories is Resource.Error -> {
                        _state.update { it.copy(error = true, loading = false) }
                    }

                    categories is Resource.Success && films is Resource.Success -> {
                        _state.update {
                            it.copy(
                                loading = false,
                                error = false,
                                categoryList = categories.data!!,
                                filmList = films.data!!
                            )
                        }
                    }
                }
            }.collect()
        }
    }


    fun getFilmsByCategories(categoryName: String){

        _state.update {
            if (it.selectedCategory == categoryName)
                it.copy(selectedCategory = null)
            else
                it.copy(selectedCategory = categoryName)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val films =
                filmRepository.getFilmsByCategories(state.value.selectedCategory)

            _state.update { it.copy(filmList = films) }
        }


    }



}