package com.example.android_practice

import FiltersDataStore
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.android_practice.cache.FilterStateCache
import com.example.android_practice.components.AppContent
import com.example.android_practice.components.BottomBar
import com.example.android_practice.components.NavigationGraph
import com.example.android_practice.data.local.AppDatabase
import com.example.android_practice.data.remote.RetrofitInstance
import com.example.android_practice.data.repository.MovieRepository
import com.example.android_practice.data.repository.ProfileRepository
import com.example.android_practice.di.appModule
import com.example.android_practice.ui.theme.androidPracticeTheme
import com.example.android_practice.viewmodel.ProfileViewModel
import com.example.android_practice.viewmodel.ViewModelFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import java.io.File
import java.io.FileOutputStream


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
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
            val filtersDataStore: FiltersDataStore = koinInject()
            val profileRepository: ProfileRepository = koinInject()

            val viewModelFactory = remember {
                ViewModelFactory(
                    repository = repository,
                    filtersDataStore = filtersDataStore,
                    db = database,
                    profileRepository = profileRepository
                )
            }

            val profileViewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
            val context = LocalContext.current



            AppContent(
                navController = navController,
                filterStateCache = filterStateCache,
                repository = repository,
                database = database
            )
        }
    }
}

private fun saveBitmapToCache(bitmap: Bitmap, context: Context): String {
    val file = File(context.cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return Uri.fromFile(file).toString()
}


