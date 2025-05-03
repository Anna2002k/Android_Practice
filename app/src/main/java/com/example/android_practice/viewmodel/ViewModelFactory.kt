package com.example.android_practice.viewmodel

import FiltersDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.repository.MovieRepository
import com.example.profile_feature.data.repository.ProfileRepository
import com.example.profile_feature.viewmodel.ProfileViewModel

class ViewModelFactory(
    private val repository: MovieRepository,
    private val filtersDataStore: FiltersDataStore,
    private val db: AppDatabase,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
  //  @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                MovieViewModel(repository, filtersDataStore, db) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(profileRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}