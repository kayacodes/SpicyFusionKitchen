package com.sfk.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SfkAppRoot() }
    }
}

@Composable
fun SfkAppRoot() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        val nav = rememberNavController()
        val tabs = listOf("home", "search", "saved", "more")

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val current = nav.currentBackStackEntryAsState().value?.destination?.route
                    tabs.forEach { route ->
                        NavigationBarItem(
                            selected = current == route,
                            onClick = {
                                nav.navigate(route) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true; restoreState = true
                                }
                            },
                            icon = { Icon(Icons.Default.Circle, contentDescription = route) },
                            label = { Text(route.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(nav, startDestination = "home", modifier = Modifier.padding(padding)) {
                composable("home") { CenteredText("Home") }
                composable("search") { CenteredText("Search") }
                composable("saved") { CenteredText("Saved") }
                composable("more") { CenteredText("More") }
            }
        }
    }
}

@Composable
private fun CenteredText(text: String) {
    Text(text = text, style = MaterialTheme.typography.headlineSmall)
}
