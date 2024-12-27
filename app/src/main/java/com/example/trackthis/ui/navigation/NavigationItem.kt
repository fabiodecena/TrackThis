package com.example.trackthis.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, var icon: ImageVector?, var title: String ) {
    data object Home: NavigationItem("home", Icons.Filled.Home, "Home")
    data object TrackDetails: NavigationItem("trackDetails", null, "Track Details")
    data object Statistics: NavigationItem("statistics", Icons.Filled.BarChart, "Statistics")
    data object History: NavigationItem("build", Icons.Filled.Build, "History")
    data object Registration: NavigationItem("profile", Icons.Filled.Person, "Registration")
    data object Settings: NavigationItem("settings", Icons.Filled.Menu, "Settings")
    data object ActiveTrackSelection: NavigationItem("activeTrack", null,"Active Tracking")
    data object InactiveTrackSelection: NavigationItem("inactiveTrack",null ,"Inactive Tracking")
    data object Welcome: NavigationItem("welcome", null, "Welcome")
    data object Login: NavigationItem("login", null, "Login")

}
val bottomBarNavigationItems = listOf(
    NavigationItem.Home,
    NavigationItem.Statistics,
    NavigationItem.History
)
val trackNavigationItems = listOf(
    NavigationItem.ActiveTrackSelection,
    NavigationItem.InactiveTrackSelection
)

