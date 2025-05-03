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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.android_practice.cache.FilterStateCache
import com.example.android_practice.content.DetailScreen
import com.example.profile_feature.ui.EditProfileScreen
import com.example.android_practice.content.FavoritesScreen
import com.example.android_practice.content.FilterScreen
import com.example.android_practice.content.MainScreen
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.repository.MovieRepository
import com.example.profile_feature.data.repository.ProfileRepository
import com.example.android_practice.entity.MovieEntity
import com.example.android_practice.viewmodel.FavoritesViewModel
import com.example.android_practice.viewmodel.FilterViewModel
import com.example.android_practice.viewmodel.MovieViewModel
import com.example.profile_feature.viewmodel.ProfileViewModel
import com.example.android_practice.viewmodel.ViewModelFactory
import com.example.profile_feature.ui.ProfileScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    filtersDataStore: FiltersDataStore,
    repository: MovieRepository,
    database: AppDatabase,
    filterStateCache: FilterStateCache
) {

    val profileRepository: ProfileRepository = koinInject()

    val viewModelFactory = remember {
        ViewModelFactory(
            repository = repository,
            filtersDataStore = filtersDataStore,
            db = database,
            profileRepository = profileRepository
        )
    }

    NavHost(navController, startDestination = "main", modifier = modifier) {
        composable("main") {
            val viewModel: MovieViewModel = koinViewModel()
            MainScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("filters") {
            val viewModel: FilterViewModel = koinViewModel()
            FilterScreen(
                navController = navController,
                viewModel = viewModel,
                filtersDataStore = koinInject(),
                filterStateCache = koinInject()
            )
        }
        composable("details/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")
            DetailScreen(
                movieId = movieId,
                navController = navController,
                viewModel = koinViewModel()
            )
        }
        composable("favorites") {
            val viewModel: FavoritesViewModel = koinViewModel()
            FavoritesScreen(navController = navController, viewModel = viewModel)
        }
        composable("editProfile") {
            val viewModel: ProfileViewModel = koinViewModel()
            EditProfileScreen(navController = navController, viewModel = viewModel)
        }
        composable("profile") {
            val viewModel: ProfileViewModel = koinViewModel()
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
    }
}

