package com.example.trackthis.data

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
    data object Build: NavigationItem("build", Icons.Filled.Build, "History")
    data object Profile: NavigationItem("profile", Icons.Filled.Person, "Profile")
    data object Settings: NavigationItem("settings", Icons.Filled.Menu, "Settings")
    data object ActiveTrackSelection: NavigationItem("activeTrack", null,"Active Tracking")
    data object InactiveTrackSelection: NavigationItem("inactiveTrack",null ,"Inactive Tracking")

}
val bottomBarNavigationItems = listOf(
    NavigationItem.Home,
    NavigationItem.Statistics,
    NavigationItem.Build
)
val trackNavigationItems = listOf(
    NavigationItem.ActiveTrackSelection,
    NavigationItem.InactiveTrackSelection
)

