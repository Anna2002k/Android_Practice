package com.example.android_practice.di

import FiltersDataStore
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.android_practice.cache.FilterStateCache
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.remote.RetrofitInstance
import com.example.android_practice.data.repository.MovieRepository
import com.example.android_practice.viewmodel.FavoritesViewModel
import com.example.android_practice.viewmodel.FilterViewModel
import com.example.android_practice.viewmodel.MovieViewModel
import com.example.profile_feature.data.repository.ProfileRepository
import com.example.android_practice.viewmodel.ViewModelFactory
import com.example.profile_feature.viewmodel.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {
    single { androidContext().dataStore }
    single { FiltersDataStore(get()) }
    single { FilterStateCache() }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app-database"
        ).build()
    }
    single { ProfileRepository(androidContext()) }
    single { MovieRepository(RetrofitInstance.movieApi, get()) }
    viewModel { MovieViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { FavoritesViewModel(get()) }
    viewModel { FilterViewModel(get(), get(), get()) }
}