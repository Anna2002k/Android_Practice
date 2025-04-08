package com.example.android_practice

import FiltersDataStore
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.android_practice.cache.FilterStateCache
import com.example.android_practice.components.AppContent
import com.example.android_practice.components.BottomBar
import com.example.android_practice.components.NavigationGraph
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.remote.RetrofitInstance
import com.example.android_practice.data.repository.MovieRepository
import com.example.android_practice.di.appModule
import com.example.android_practice.ui.theme.androidPracticeTheme
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startKoin {
            androidContext(this@MainActivity)
            modules(
                appModule,
                module {
                    single { MovieRepository(RetrofitInstance.movieApi, get()) }
                }
            )
        }

        setContent {
            AppKoinWrapper()
        }
    }
}
@Composable
fun AppKoinWrapper() {
    KoinAndroidContext {
        androidPracticeTheme {
            val navController = rememberNavController()
            val filterStateCache: FilterStateCache = koinInject()
            val repository: MovieRepository = koinInject()
            val database: AppDatabase = koinInject()

            AppContent(
                navController = navController,
                filterStateCache = filterStateCache,
                repository = repository,
                database = database
            )
        }
    }
}



