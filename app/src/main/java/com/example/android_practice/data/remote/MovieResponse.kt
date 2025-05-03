package com.example.android_practice.data.remote

import com.example.android_practice.entity.MovieEntity
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("docs") val movies: List<NetworkMovie>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int
) {
    data class NetworkMovie(
        val id: Int,
        val name: String,
        val year: Int,
        val description: String?,
        val rating: Rating,
        val poster: Poster,
        val genres: List<Genre>,
        val countries: List<Country>
    ) {
        data class Rating(
            @SerializedName("kp") val value: Double
        )

        data class Poster(
            @SerializedName("url") val url: String
        )

        data class Genre(
            val name: String
        )

        data class Country(
            val name: String
        )

        fun toMovieEntity(): MovieEntity {
            return MovieEntity(
                id = this.id,
                name = this.name,
                year = this.year,
                description = this.description,
                rating = this.rating.value,
                posterUrl = this.poster.url,
                genres = this.genres.map { it.name },
                countries = this.countries.map { it.name }
            )
        }
    }
}
