package com.sfk.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sfk.app.ui.components.bottombar.BottomTab
import com.sfk.app.ui.components.bottombar.sfkBottomBar
import com.sfk.app.ui.theme.SfkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SfkAppRoot() }
    }
}

@Suppress("FunctionNaming")
@Composable
fun SfkAppRoot() {
    SfkTheme {
        val nav = rememberNavController()
        val current = nav.currentBackStackEntryAsState().value?.destination?.route ?: "home"

        val tabs = listOf(
            BottomTab("home", Icons.Outlined.Home, "Home"),
            BottomTab("search", Icons.Outlined.Search, "Search"),
            BottomTab("saved", Icons.Outlined.LocalDining, "Saved"),
            BottomTab("more", Icons.Outlined.Person, "More")
        )

        Scaffold(
            bottomBar = {
                sfkBottomBar(
                    tabs = tabs,
                    selectedRoute = current,
                    onTabSelected = { route ->
                        nav.navigate(route) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = nav,
                startDestination = "home",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") { Text("Home", style = MaterialTheme.typography.headlineSmall) }
                composable("search") { Text("Search", style = MaterialTheme.typography.headlineSmall) }
                composable("saved") { Text("Saved", style = MaterialTheme.typography.headlineSmall) }
                composable("more") { Text("More", style = MaterialTheme.typography.headlineSmall) }
            }
        }
    }
}
