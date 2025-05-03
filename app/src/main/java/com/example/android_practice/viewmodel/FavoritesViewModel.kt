package com.example.android_practice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.repository.MovieRepository
import com.example.android_practice.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _favoriteMovies = MutableStateFlow<List<MovieEntity>>(emptyList())
    val favoriteMovies: StateFlow<List<MovieEntity>> = _favoriteMovies.asStateFlow()

    init {
        loadFavoriteMovies()
    }

    private fun loadFavoriteMovies() {
        viewModelScope.launch {
            repository.getFavoriteMovies().collect {
                _favoriteMovies.value = it
            }
        }
    }

    fun removeFromFavorites(movie: MovieEntity) {
        viewModelScope.launch {
            repository.removeFromFavorites(movie)

        }
    }
}