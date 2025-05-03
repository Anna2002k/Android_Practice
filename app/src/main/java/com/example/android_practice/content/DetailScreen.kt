package com.example.android_practice.content

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.android_practice.R
import com.example.android_practice.components.GlideImage
import com.example.android_practice.entity.MovieEntity
import com.example.android_practice.ui.theme.androidPracticeTheme
import com.example.android_practice.viewmodel.MovieViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int?,
    navController: NavController,
    viewModel: MovieViewModel = koinViewModel()
) {
    LaunchedEffect(movieId) {
        movieId?.let { viewModel.getMovieById(it) }
    }

    val movieDetailsState by viewModel.movieDetailsState.collectAsState(initial = MovieViewModel.MovieDetailsState.Loading)
    val isFavorite by viewModel.isFavorite(movieId ?: -1).collectAsState(initial = false)

    val movie = (movieDetailsState as? MovieViewModel.MovieDetailsState.Success)?.movie
    if (movie == null) {
        Text(text = "Ошибка загрузки данных", modifier = Modifier.padding(16.dp))
        return
    }

    androidPracticeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Детали фильма") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Назад")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.toggleFavorite(movie) }) {
                            Icon(
                                painter = painterResource(
                                    id = if (isFavorite) R.drawable.ic_favorite_filled
                                    else R.drawable.ic_favorite_border
                                ),
                                contentDescription = "Избранное",
                                tint = if (isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                )
            }
        ) { padding ->
            MovieDetailsContent(
                movie = movie,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: MovieEntity,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        GlideImage(url = movie.posterUrl, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.name, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Год: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Рейтинг: ${movie.rating}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))
        movie.genres?.let {
            Text(text = "Жанры: ${it.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }
        movie.countries?.let {
            Text(text = "Страны: ${it.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(
            text = movie.formattedDescription(),
            style = MaterialTheme.typography.bodySmall
        )
    }
}


