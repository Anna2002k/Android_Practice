package com.example.android_practice.components

import FiltersDataStore
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import com.example.android_practice.cache.FilterStateCache
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.remote.RetrofitInstance
import com.example.android_practice.data.repository.MovieRepository
import org.koin.compose.koinInject


@Composable
fun AppContent(
    navController: NavHostController,
    filterStateCache: FilterStateCache,
    repository: MovieRepository,
    database: AppDatabase,
    modifier: Modifier = Modifier
) {
    val filtersDataStore: FiltersDataStore = koinInject()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController, state = true)
        }
    ) { paddingValues ->
        NavigationGraph(
            navController = navController,
            filtersDataStore = filtersDataStore,
            repository = repository,
            database = database,
            filterStateCache = filterStateCache,
            modifier = Modifier.padding(paddingValues)
        )
    }
}