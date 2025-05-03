package com.example.android_practice.data.repository

import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.remote.MovieApi
import com.example.android_practice.data.remote.RetrofitInstance.API_KEY
import com.example.android_practice.entity.FavoriteMovieEntity
import com.example.android_practice.entity.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepository(
    private val api: MovieApi,
    private val db: AppDatabase
) {
    private val favoriteDao = db.favoriteMovieDao()

    suspend fun addToFavorites(movie: MovieEntity) {
        withContext(Dispatchers.IO) {
            favoriteDao.insert(FavoriteMovieEntity(movie))
        }
    }

    suspend fun removeFromFavorites(movie: MovieEntity) {
        withContext(Dispatchers.IO) {
            favoriteDao.delete(FavoriteMovieEntity(movie))
        }
    }

    fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return favoriteDao.getAll().map { favorites ->
            favorites.map { it.toMovieEntity() }
        }
    }

    fun isFavorite(id: Int): Flow<Boolean> {
        return favoriteDao.isFavorite(id)
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

    suspend fun getMovieById(movieId: Int): MovieEntity {
        return api.getMovieById(
            apiKey = API_KEY,
            movieId = movieId,
            selectFields = listOf(
                "id", "name", "year", "description", "rating",
                "poster", "genres", "countries"
            ),
            notNullFields = listOf(
                "id", "name", "year", "description", "rating.kp",
                "poster.url", "genres.name", "countries.name"
            )
        ).toMovieEntity()
    }
}