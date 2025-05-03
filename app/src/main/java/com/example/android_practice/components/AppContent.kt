package com.example.android_practice.components

import FiltersDataStore
import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
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
import com.example.android_practice.ui.theme.BottomNavigationItems
import org.koin.compose.koinInject


@Composable
fun AppContent(
    navController: NavHostController
) {
    val filterStateCache: FilterStateCache = koinInject()
    val repository: MovieRepository = koinInject()
    val database: AppDatabase = koinInject()
    val filtersDataStore: FiltersDataStore = koinInject()

    val bottomNavItems = listOf(
        BottomNavigationItems.MainScreen,
        BottomNavigationItems.FavoritesScreen,
        BottomNavigationItems.ProfileScreen
    )

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController, screens = bottomNavItems)
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