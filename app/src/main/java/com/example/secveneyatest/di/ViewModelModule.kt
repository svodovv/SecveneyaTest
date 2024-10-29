package com.example.secveneyatest.di

import androidx.lifecycle.SavedStateHandle
import com.example.secveneyatest.domain.model.FilmDetailModel
import com.example.secveneyatest.presentation.ui.film.detail.FilmDetailViewModel
import com.example.secveneyatest.presentation.ui.film.list.FilmListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { FilmListViewModel(get()) }

    viewModel {
        (handle: SavedStateHandle) -> FilmDetailViewModel(handle, get())
    }

}