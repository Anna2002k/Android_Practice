package com.example.android_practice.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android_practice.content.DetailScreen
import com.example.android_practice.content.MainScreen
import com.example.android_practice.content.Screen1
import com.example.android_practice.entity.MovieEntity

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    NavHost(navController, startDestination = "main", modifier = modifier) {
        composable("main") {
            MainScreen( navController = navController)
        }
        composable("details/{movieId}") { backStackEntry ->
            //val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            val movie = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<MovieEntity>("movie")

            if (movie != null) {
                DetailScreen(movie = movie, navController = navController)
            } else {
                Text(text = "Ошибка загрузки данных", modifier = Modifier.padding(16.dp))
            }
        }
        composable("screen1") { Screen1(navController) }
    }
}

