package com.example.android_practice.di

import com.example.android_practice.entity.MovieEntity

data class MovieUiModel(
    val id: Int,
    val title: String,
    val year: String,
    val rating: String,
    val genres: String,
    val posterUrl: String
)
fun MovieEntity.toUiModel(): MovieUiModel {
    return MovieUiModel(
        id = id,
        title = name,
        year = "Год: $year",
        rating = "Рейтинг: $rating",
        genres = genres?.joinToString(", ") ?: "",
        posterUrl = posterUrl
    )
}