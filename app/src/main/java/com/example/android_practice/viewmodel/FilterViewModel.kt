package com.example.android_practice.viewmodel

import FilterPreferences
import FiltersDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.cache.FilterStateCache
import com.example.android_practice.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FilterViewModel(
    private val filtersDataStore: FiltersDataStore,
    private val filterStateCache: FilterStateCache,
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow(filterStateCache.currentState.query)
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenres = MutableStateFlow(filterStateCache.currentState.genres)
    val selectedGenres: StateFlow<Set<String>> = _selectedGenres.asStateFlow()

    private val _minRating = MutableStateFlow(filterStateCache.currentState.minRating)
    val minRating: StateFlow<Double> = _minRating.asStateFlow()

    private val _allGenres = MutableStateFlow<List<String>>(emptyList())
    val allGenres: StateFlow<List<String>> = _allGenres.asStateFlow()

    init {
        loadAllGenres()
        viewModelScope.launch {
            searchQuery.collectLatest { filterStateCache.updateState(it, selectedGenres.value, minRating.value) }
        }
        viewModelScope.launch {
            selectedGenres.collectLatest { filterStateCache.updateState(searchQuery.value, it, minRating.value) }
        }
        viewModelScope.launch {
            minRating.collectLatest { filterStateCache.updateState(searchQuery.value, selectedGenres.value, it) }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onGenreSelected(genre: String, isChecked: Boolean) {
        _selectedGenres.value = _selectedGenres.value.toMutableSet().apply {
            if (isChecked) add(genre) else remove(genre)
        }
    }

    fun onMinRatingChange(rating: Double) {
        _minRating.value = rating
    }

    fun resetFilters() {
        _searchQuery.value = ""
        _selectedGenres.value = emptySet()
        _minRating.value = 0.0
        filterStateCache.reset()
    }

    fun applyFilters() {
        viewModelScope.launch {
            filtersDataStore.saveFilters(searchQuery.value, selectedGenres.value, minRating.value)
        }
    }

    private fun loadAllGenres() {
        viewModelScope.launch {
            val allMovies = repository.getMovies(1)
            _allGenres.value = getAllGenres(allMovies)
        }
    }

    private fun getAllGenres(movies: List<com.example.android_practice.entity.MovieEntity>): List<String> {
        return movies.flatMap { it.genres ?: emptyList() }.distinct()
    }
}