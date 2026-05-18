package com.filmapp.controller

import com.filmapp.model.Film
import com.filmapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi

class FilmController {

    @OptIn(InternalSerializationApi::class)
    suspend fun getAllFilms(): Result<List<Film>> = withContext(Dispatchers.IO) {
        runCatching { ApiService.getAllFilms() }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun getFilmById(id: String): Result<Film> = withContext(Dispatchers.IO) {
        runCatching { ApiService.getFilmById(id) }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun createFilm(film: Film): Result<Film> = withContext(Dispatchers.IO) {
        runCatching { ApiService.createFilm(film) }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun updateFilm(id: String, film: Film): Result<Film> = withContext(Dispatchers.IO) {
        runCatching { ApiService.updateFilm(id, film) }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun deleteFilm(id: String): Result<Film> = withContext(Dispatchers.IO) {
        runCatching { ApiService.deleteFilm(id) }
    }
}
