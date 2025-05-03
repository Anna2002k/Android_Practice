package com.example.android_practice.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val year: Int,
    val description: String?,
    val rating: Double,
    val posterUrl: String,
    val genres: List<String>,
    val countries: List<String>
) {
    constructor(movie: MovieEntity) : this(
        id = movie.id,
        name = movie.name,
        year = movie.year,
        description = movie.description,
        rating = movie.rating,
        posterUrl = movie.posterUrl,
        genres = movie.genres ?: emptyList(),
        countries = movie.countries ?: emptyList()
    )

    fun toMovieEntity(): MovieEntity {
        return MovieEntity(
            id = id,
            name = name,
            year = year,
            description = description,
            rating = rating,
            posterUrl = posterUrl,
            genres = genres,
            countries = countries
        )
    }
}