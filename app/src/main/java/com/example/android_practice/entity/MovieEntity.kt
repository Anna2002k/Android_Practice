package com.example.android_practice.entity

data class MovieEntity(
    val id: Int,
    val name: String,
    val year: Int,
    val description: String?,
    val rating: Double,
    val posterUrl: String,
    val genres: List<String>,
    val countries: List<String>
)

