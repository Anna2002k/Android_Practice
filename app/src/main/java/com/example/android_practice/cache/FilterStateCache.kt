package com.example.android_practice.cache

class FilterStateCache {
    var hasActiveFilters: Boolean = false
        private set

    fun updateState(query: String, genres: Set<String>, rating: Double) {
        hasActiveFilters = query.isNotEmpty() || genres.isNotEmpty() || rating > 0.0
    }

    fun reset() {
        hasActiveFilters = false
    }
}