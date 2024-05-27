package com.example.trackthis.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector?, var title: String ) {
    data object Home: NavigationItem("home", Icons.Filled.Home, "Home")
    data object Build: NavigationItem("build", Icons.Filled.Build, "Build")
    data object Location: NavigationItem("location", Icons.Filled.LocationOn, "Location")
    data object Profile: NavigationItem("profile", Icons.Filled.Person, "Profile")
    data object Settings: NavigationItem("settings", Icons.Filled.Menu, "Settings")
    data object ActiveTrack: NavigationItem("activeTrack", null,"Active Tracking")
    data object InactiveTrack: NavigationItem("inactiveTrack",null ,"Inactive Tracking")

}
val topBarNavigationItems = listOf(
    NavigationItem.Settings,
    NavigationItem.Profile

)
val bottomBarNavigationItems = listOf(
    NavigationItem.Home,
    NavigationItem.Build,
    NavigationItem.Location
)
val trackNavigationItems = listOf(
    NavigationItem.ActiveTrack,
    NavigationItem.InactiveTrack
)
