package com.filmapp.network

import com.filmapp.model.Film
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json

object ApiService {

    private const val BASE_URL = "https://68ff8dfbe02b16d1753e765d.mockapi.io/film"

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                coerceInputValues = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    @OptIn(InternalSerializationApi::class)
    suspend fun getAllFilms(): List<Film> = client.get(BASE_URL).body()

    @OptIn(InternalSerializationApi::class)
    suspend fun getFilmById(id: String): Film = client.get("$BASE_URL/$id").body()

    @OptIn(InternalSerializationApi::class)
    suspend fun createFilm(film: Film): Film = client.post(BASE_URL) {
        contentType(ContentType.Application.Json)
        setBody(film)
    }.body()

    @OptIn(InternalSerializationApi::class)
    suspend fun updateFilm(id: String, film: Film): Film = client.put("$BASE_URL/$id") {
        contentType(ContentType.Application.Json)
        setBody(film)
    }.body()

    @OptIn(InternalSerializationApi::class)
    suspend fun deleteFilm(id: String): Film = client.delete("$BASE_URL/$id").body()
}
