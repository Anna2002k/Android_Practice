package com.example.android_practice

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.android_practice.components.AppContent
import com.example.android_practice.di.appModule
import com.example.android_practice.ui.theme.androidPracticeTheme
import com.example.profile_feature.di.profileModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.context.GlobalContext.startKoin
import java.io.File
import java.io.FileOutputStream


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule, profileModule)
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

            AppContent(
                navController = navController
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


