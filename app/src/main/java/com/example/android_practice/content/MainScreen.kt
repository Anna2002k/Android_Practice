package com.example.android_practice.content


import android.content.Context
import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android_practice.data.MovieData
import com.example.android_practice.entity.MovieEntity
import androidx.compose.ui.viewinterop.AndroidView
import com.example.android_practice.ui.theme.Android_PracticeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val movies = remember { MovieData.movies }

    Android_PracticeTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text(text = "Список фильмов") })
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie, onClick = {
                        navController.navigate("details/${movie.id}")
                    })
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: MovieEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlideImage(url = movie.posterUrl)

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(text = movie.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Год: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Рейтинг: ${movie.rating}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                movie.genres?.let {
                    Text(text = "Жанры: ${it.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun GlideImage(url: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            ImageView(ctx).apply {
                Glide.with(ctx)
                    .load(url)
                    .apply(RequestOptions().centerInside())
                    .into(this)
            }
        }
    )
}

