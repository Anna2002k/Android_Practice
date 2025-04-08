package com.example.android_practice.components

import FiltersDataStore
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android_practice.cache.FilterStateCache
import com.example.android_practice.content.DetailScreen
import com.example.android_practice.content.FavoritesScreen
import com.example.android_practice.content.FilterScreen
import com.example.android_practice.content.MainScreen
import com.example.android_practice.content.Screen1
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.remote.RetrofitInstance
import com.example.android_practice.data.repository.MovieRepository
import com.example.android_practice.entity.MovieEntity
import com.example.android_practice.viewmodel.MovieViewModel
import com.example.android_practice.viewmodel.ViewModelFactory

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    filtersDataStore: FiltersDataStore,
    repository: MovieRepository,
    database: AppDatabase,
    filterStateCache: FilterStateCache
) {
    val viewModelFactory = remember {
        ViewModelFactory(repository, filtersDataStore, database)
    }

    NavHost(navController, startDestination = "main", modifier = modifier) {
        composable("main") {
            val viewModel: MovieViewModel = viewModel(factory = viewModelFactory)
            MainScreen(
                navController = navController,
                viewModel = viewModel,
                filterStateCache = filterStateCache
            )
        }
        composable("filters") {
            val viewModel: MovieViewModel = viewModel(factory = viewModelFactory)
            FilterScreen(
                navController = navController,
                viewModel = viewModel,
                filtersDataStore = filtersDataStore,
                filterStateCache = filterStateCache
            )
        }
        composable("details/{movieId}") { backStackEntry ->
            //val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            val viewModel: MovieViewModel = viewModel(factory = viewModelFactory)
            val movie = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<MovieEntity>("movie")

            if (movie != null) {
                DetailScreen(
                    movie = movie,
                    navController = navController,
                    viewModel = viewModel
                )
            } else {
                Text(text = "Ошибка загрузки данных", modifier = Modifier.padding(16.dp))
            }
        }
        composable("favorites") {
            val viewModel: MovieViewModel = viewModel(factory = viewModelFactory)
            FavoritesScreen(navController, viewModel)
        }
        composable("screen1") { Screen1(navController) }
    }
}

