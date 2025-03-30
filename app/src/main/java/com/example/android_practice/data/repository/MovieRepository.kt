package com.example.android_practice.data.repository

import com.example.android_practice.data.remote.MovieApi
import com.example.android_practice.entity.MovieEntity

class MovieRepository(private val api: MovieApi) {
    companion object {
        private const val API_KEY = "3K4TQ6S-ZX8MZGH-JX94R32-E2YMFJM"
    }

    suspend fun getMovies(page: Int): List<MovieEntity> {
        return api.getMovies(
            apiKey = API_KEY,
            page = page,
            limit = 10,
            selectFields = listOf(
                "id", "name", "year", "description", "rating",
                "poster", "genres", "countries"
            ),
            notNullFields = listOf(
                "id", "name", "year", "description", "rating.kp",
                "poster.url", "genres.name", "countries.name"
            ),
            typeNumber = 1,
            ratingKp = "1-10",
            ageRating = "!18"
        ).movies.map { it.toMovieEntity() }
    }
}