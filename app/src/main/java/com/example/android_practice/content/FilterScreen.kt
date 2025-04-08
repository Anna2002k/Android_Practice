package com.example.android_practice.content

import FilterPreferences
import FiltersDataStore
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.android_practice.cache.FilterStateCache
import com.example.android_practice.viewmodel.MovieState
import com.example.android_practice.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    viewModel: MovieViewModel,
    filtersDataStore: FiltersDataStore,
    filterStateCache: FilterStateCache
) {
    val movieState by viewModel.movieState.collectAsState()
    val filters by filtersDataStore.filtersFlow.collectAsState(initial = FilterPreferences("", emptySet(), 0.0))
    var searchQuery by remember { mutableStateOf(filters.query) }
    var selectedGenres by remember { mutableStateOf(filters.genres) }
    var minRating by remember { mutableStateOf(filters.minRating) }

    val allGenres = remember(movieState) {
        if (movieState is MovieState.Success) {
            (movieState as MovieState.Success).genres
        } else {
            emptyList()
        }
    }

    LaunchedEffect(searchQuery, selectedGenres, minRating) {
        filterStateCache.updateState(searchQuery, selectedGenres, minRating)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильтры") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            filterStateCache.reset()
                            searchQuery = ""
                            selectedGenres = emptySet()
                            minRating = 0.0
                        }
                    ) {
                        Text("Сбросить")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.applyFilters(searchQuery, selectedGenres, minRating)
                    navController.popBackStack()
                },
                icon = { Icon(Icons.Default.Done, contentDescription = "Применить") },
                text = { Text("Готово") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Divider()

            Text("Жанры:", modifier = Modifier.padding(16.dp))
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .heightIn(max = 300.dp)
            ) {
                items(allGenres) { genre ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = selectedGenres.contains(genre),
                            onCheckedChange = { isChecked ->
                                selectedGenres = if (isChecked) {
                                    selectedGenres + genre
                                } else {
                                    selectedGenres - genre
                                }
                            }
                        )
                        Text(genre)
                    }
                }
            }

            Divider()

            Text(
                text = "Минимальный рейтинг: ${"%.1f".format(minRating)}",
                modifier = Modifier.padding(16.dp)
            )
            Slider(
                value = minRating.toFloat(),
                onValueChange = { minRating = it.toDouble() },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}