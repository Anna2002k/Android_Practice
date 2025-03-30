package com.example.android_practice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.repository.MovieRepository
import com.example.android_practice.entity.MovieEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MovieState {
    data object Loading : MovieState()
    data class Success(val movies: List<MovieEntity>) : MovieState()
    data class Error(val message: String) : MovieState()
}

class MovieViewModel(
    private val repository: MovieRepository
) : ViewModel() {
    private val _movieState = MutableStateFlow<MovieState>(MovieState.Loading)
    val movieState: StateFlow<MovieState> = _movieState.asStateFlow()

    init {
        loadMovies()
    }

    fun loadMovies(page: Int = 1) {
        viewModelScope.launch {
            _movieState.value = MovieState.Loading
            try {
                val movies = repository.getMovies(page)
                _movieState.value = MovieState.Success(movies)
            } catch (e: Exception) {
                _movieState.value = MovieState.Error(e.message ?: "Unknown error")
            }
        }
    }
}