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
import org.koin.android.ext.koin.androidContext
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
}