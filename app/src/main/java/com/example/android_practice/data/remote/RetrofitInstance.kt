
package com.example.android_practice.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.kinopoisk.dev/"
    const val API_KEY = "3K4TQ6S-ZX8MZGH-JX94R32-E2YMFJM"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val movieApi: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }
}