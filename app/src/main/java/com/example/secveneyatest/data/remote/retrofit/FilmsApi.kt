package com.example.secveneyatest.data.remote.retrofit

import com.example.secveneyatest.data.remote.dto.FilmsDto
import retrofit2.http.GET

interface FilmsApi {

    @GET("/sequeniatesttask/films.json")
    suspend fun getFilms(): FilmsDto?
}