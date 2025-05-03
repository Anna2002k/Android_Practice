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
import com.example.android_practice.viewmodel.FilterViewModel
import com.example.android_practice.viewmodel.MovieState
import com.example.android_practice.viewmodel.MovieViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    viewModel: FilterViewModel = koinViewModel(),
    filtersDataStore: FiltersDataStore = koinInject(),
    filterStateCache: FilterStateCache = koinInject()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGenres by viewModel.selectedGenres.collectAsState()
    val minRating by viewModel.minRating.collectAsState()
    val allGenres by viewModel.allGenres.collectAsState()

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
                        onClick = { viewModel.resetFilters() }
                    ) {
                        Text("Сбросить")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.applyFilters()
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
                onValueChange = { viewModel.onSearchQueryChange(it) },
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
                                viewModel.onGenreSelected(genre, isChecked)
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
                onValueChange = { viewModel.onMinRatingChange(it.toDouble()) },
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}