package com.example.android_practice.content

import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android_practice.R
import com.example.android_practice.data.MovieData
import com.example.android_practice.ui.theme.Android_PracticeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(movieId: Int, navController: NavController) {
    val movie = MovieData.movies.find { it.id == movieId }
    val isFavorite = remember { mutableStateOf(false) }

    if (movie == null) {
        Text(text = "Фильм не найден", modifier = Modifier.padding(16.dp))
        return
    }

    Android_PracticeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = movie.name) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Назад")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isFavorite.value = !isFavorite.value }) {
                            Icon(
                                painter = painterResource(id = if (isFavorite.value) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border),
                                contentDescription = "Избранное",
                                tint = if (isFavorite.value) Color.Red else Color.Gray
                            )
                        }
                    }
                )
            }
        ) { padding ->
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                val (poster, title, year, description, rating, genres, countries) = createRefs()

                GlideImage(url = movie.posterUrl, modifier = Modifier.constrainAs(poster) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

                Text(
                    text = movie.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(poster.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Год: ${movie.year}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(year) {
                        top.linkTo(title.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Рейтинг: ${movie.rating}",
                    modifier = Modifier.constrainAs(rating) {
                        top.linkTo(year.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }
                )

                Text(
                    text = "Описание: ${movie.description ?: "Описание отсутствует"}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.constrainAs(description) {
                        top.linkTo(rating.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                movie.genres?.let {
                    Text(
                        text = "Жанры: ${it.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.constrainAs(genres) {
                            top.linkTo(description.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                        }
                    )
                }

                movie.countries?.let {
                    Text(
                        text = "Страны: ${it.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.constrainAs(countries) {
                            top.linkTo(genres.bottom, margin = 16.dp)
                            start.linkTo(parent.start)
                        }
                    )
                }
            }
        }
    }
}




