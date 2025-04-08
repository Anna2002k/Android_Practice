package com.example.android_practice.viewmodel

import FiltersDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.repository.MovieRepository

class ViewModelFactory(
    private val repository: MovieRepository,
    private val filtersDataStore: FiltersDataStore,
    private val db: AppDatabase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(repository, filtersDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}