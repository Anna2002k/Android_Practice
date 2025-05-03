package com.example.android_practice.ui.theme

sealed class Routes (val route: String){
    object MainScreen : Routes("welcomeScreen")
}