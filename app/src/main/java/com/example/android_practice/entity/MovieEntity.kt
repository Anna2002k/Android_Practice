package com.example.android_practice.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieEntity(
    val id: Int,
    val name: String,
    val year: Int,
    val description: String?,
    val rating: Double,
    val posterUrl: String,
    val genres: List<String>,
    val countries: List<String>
): Parcelable

