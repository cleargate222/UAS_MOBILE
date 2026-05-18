package com.filmapp.controller

import com.filmapp.model.Film
import com.filmapp.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilmController {

    suspend fun getAllFilms(): Result<List<Film>> = withContext(Dispatchers.IO) {
        runCatching { ApiService.getAllFilms() }
    }

    suspend fun getFilmById(id: String): Result<Film> = withContext(Dispatchers.IO) {
        runCatching { ApiService.getFilmById(id) }
    }

    suspend fun createFilm(film: Film): Result<Film> = withContext(Dispatchers.IO) {
        runCatching { ApiService.createFilm(film) }
    }

    suspend fun updateFilm(id: String, film: Film): Result<Film> = withContext(Dispatchers.IO) {
        runCatching { ApiService.updateFilm(id, film) }
    }

    suspend fun deleteFilm(id: String): Result<Film> = withContext(Dispatchers.IO) {
        runCatching { ApiService.deleteFilm(id) }
    }
}
