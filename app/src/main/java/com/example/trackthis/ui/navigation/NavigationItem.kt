package com.example.trackthis.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class representing navigation items in the application.
 *
 * Each navigation item has a [route] which is used for navigation, an optional [icon]
 * to display in UI elements, and a [title] for display purposes.
 */
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
/**
 * List of navigation items to be displayed in the bottom navigation bar.
 */
val bottomBarNavigationItems = listOf(
    NavigationItem.Home,
    NavigationItem.Statistics,
    NavigationItem.History
)
/**
 * List of navigation items related to track selection.
 */
val trackNavigationItems = listOf(
    NavigationItem.ActiveTrackSelection,
    NavigationItem.InactiveTrackSelection
)

