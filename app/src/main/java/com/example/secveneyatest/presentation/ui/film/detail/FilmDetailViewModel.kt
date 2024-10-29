package com.example.secveneyatest.presentation.ui.film.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.secveneyatest.domain.model.FilmDetailModel
import com.example.secveneyatest.domain.repository.FilmRepository
import com.example.secveneyatest.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilmDetailViewModel(
    stateHandle: SavedStateHandle,
    private val filmRepository: FilmRepository
) : ViewModel() {

    private val filmId: Int = stateHandle[Constants.FILM_ID_KEY] ?: 0

    private val _state = MutableStateFlow<FilmDetailModel?>(null)
    val state = _state.asStateFlow()


    init {
        getFilm()
    }


    private fun getFilm(){
        viewModelScope.launch(Dispatchers.IO) {
            val film = filmRepository.getFilmById(filmId)
            _state.value = film
        }
    }

}