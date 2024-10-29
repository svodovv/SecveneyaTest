package com.example.secveneyatest.di

import com.example.secveneyatest.data.repository.FilmRepositoryImpl
import com.example.secveneyatest.domain.repository.FilmRepository
import org.koin.dsl.module


val repositoryModule = module {
    single <FilmRepository>{ FilmRepositoryImpl(get()) }
}