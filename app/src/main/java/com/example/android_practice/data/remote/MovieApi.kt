package com.example.android_practice.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("v1.4/movie")
    suspend fun getMovies(
        @Header("X-API-KEY") apiKey: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("selectFields") selectFields: List<String>,
        @Query("notNullFields") notNullFields: List<String>,
        @Query("typeNumber") typeNumber: Int,
        @Query("rating.kp") ratingKp: String,
        @Query("ageRating") ageRating: String
    ): MovieResponse

    @GET("v1.4/movie/{id}")
    suspend fun getMovieById(
        @Header("X-API-KEY") apiKey: String,
        @Path("id") movieId: Int,
        @Query("selectFields") selectFields: List<String> = listOf(
            "id", "name", "year", "description", "rating",
            "poster", "genres", "countries"
        ),
        @Query("notNullFields") notNullFields: List<String> = listOf(
            "id", "name", "year", "description", "rating.kp",
            "poster.url", "genres.name", "countries.name"
        )
    ): MovieResponse.NetworkMovie
}