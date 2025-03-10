package com.example.android_practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.android_practice.components.BottomBar
import com.example.android_practice.components.NavigationGraph
import com.example.android_practice.ui.theme.Android_PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            Android_PracticeTheme {
                Scaffold(
                    bottomBar = {
                        BottomBar(navController = navController, state = true)
                    }
                ) { paddingValues ->
                    NavigationGraph(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}



