package com.example.android_practice.viewmodel

import FilterPreferences
import FiltersDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.repository.MovieRepository
import com.example.android_practice.entity.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class MovieState {
    data object Loading : MovieState()
    data class Success(val movies: List<MovieEntity>, val genres: List<String>) : MovieState()
    data class Error(val message: String) : MovieState()
}

fun getAllGenres(movies: List<MovieEntity>): List<String> {
    return movies.flatMap { it.genres ?: emptyList() }.distinct()
}

class MovieViewModel(
    private val repository: MovieRepository,
    private val filtersDataStore: FiltersDataStore,
    private val db: AppDatabase
) : ViewModel() {

    private val _currentMovieId = MutableStateFlow<Int?>(null)
    val currentMovieId: StateFlow<Int?> = _currentMovieId.asStateFlow()

    private val _movieDetailsState = MutableStateFlow<MovieDetailsState?>(null)
    val movieDetailsState: StateFlow<MovieDetailsState?> = _movieDetailsState.asStateFlow()

    private val _hasActiveFilters = MutableStateFlow(false)
    val hasActiveFilters: StateFlow<Boolean> = _hasActiveFilters.asStateFlow()

    fun setMovieId(id: Int) {
        _currentMovieId.value = id
    }

    fun getMovieById(movieId: Int) {
        _movieDetailsState.value = MovieDetailsState.Loading
        viewModelScope.launch {
            try {
                val movie = repository.getMovieById(movieId)
                _movieDetailsState.value = MovieDetailsState.Success(movie)
            } catch (e: Exception) {
                _movieDetailsState.value = MovieDetailsState.Error("Ошибка загрузки деталей фильма: ${e.localizedMessage ?: "Неизвестная ошибка"}")
            }
        }
    }


    sealed class MovieDetailsState {
        object Loading : MovieDetailsState()
        data class Success(val movie: MovieEntity) : MovieDetailsState()
        data class Error(val message: String) : MovieDetailsState()
    }

    fun toggleFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            val isFavorite = repository.isFavorite(movie.id).first()
            if (isFavorite) {
                repository.removeFromFavorites(movie)
            } else {
                repository.addToFavorites(movie)
            }
        }
    }

    fun getFavoriteMovies(): Flow<List<MovieEntity>> {
        return repository.getFavoriteMovies()
    }

    fun isFavorite(id: Int): Flow<Boolean> {
        return repository.isFavorite(id)
    }

    private val _movieState = MutableStateFlow<MovieState>(MovieState.Loading)
    val movieState: StateFlow<MovieState> = _movieState.asStateFlow()

    private var currentFilters = FilterPreferences("", emptySet(), 0.0)

    init {
        loadMovies()
        viewModelScope.launch {
            filtersDataStore.filtersFlow.collect { filters ->
                currentFilters = filters
                _hasActiveFilters.value = filters.query.isNotEmpty() || filters.genres.isNotEmpty() || filters.minRating > 0.0
                loadMovies()
            }
        }
    }

    fun applyFilters(query: String, genres: Set<String>, minRating: Double) {
        viewModelScope.launch {
            filtersDataStore.saveFilters(query, genres, minRating)
        }
    }

    private fun loadMovies(page: Int = 1) {
        viewModelScope.launch {
            _movieState.value = MovieState.Loading
            try {
                var movies = repository.getMovies(page)

                movies = movies.filter { movie ->
                    movie.name.contains(currentFilters.query, ignoreCase = true) &&
                            (currentFilters.genres.isEmpty() || movie.genres?.any { it in currentFilters.genres } == true) &&
                            (movie.rating >= currentFilters.minRating)
                }
                val genres = getAllGenres(movies)
                _movieState.value = MovieState.Success(movies, genres)
            } catch (e: Exception) {
                _movieState.value = MovieState.Error(e.message ?: "Unknown error")
            }
        }
    }
}