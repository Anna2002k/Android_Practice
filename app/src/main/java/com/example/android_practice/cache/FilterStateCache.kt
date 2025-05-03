package com.example.android_practice.cache

import FilterPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class FilterStateCache {

    var currentState by mutableStateOf(FilterPreferences())
        private set

    val hasActiveFilters: Boolean
        get() = currentState.query.isNotEmpty() || currentState.genres.isNotEmpty() || currentState.minRating > 0.0

    fun updateState(query: String, genres: Set<String>, rating: Double) {
        currentState = FilterPreferences(query = query, genres = genres, minRating = rating)
    }

    fun reset() {
        currentState = FilterPreferences()
    }
}