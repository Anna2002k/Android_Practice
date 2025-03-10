package com.example.android_practice.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class BottomNavigationItems(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val iconSize: Dp
) {
    object MainScreen : BottomNavigationItems(
        route = "main",
        title = "Список",
        icon = Icons.Outlined.List,
        iconSize = 20.dp
    )

    object Screen1 : BottomNavigationItems(
        route = "screen1",
        title = "Профиль",
        icon = Icons.Outlined.Person,
        iconSize = 20.dp
    )
}

