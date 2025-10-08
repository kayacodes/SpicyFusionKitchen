package com.sfk.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColors = lightColorScheme(
    primary = CornellRed,
    secondary = ForestGreen,
    tertiary = Asparagus,
    background = BabyPowder,
    surface = Timberwolf,
    onPrimary = BabyPowder,
    onSecondary = BabyPowder,
    onBackground = Black,
    onSurface = Black
)

@Suppress("FunctionNaming")
@Composable
fun SfkTheme(
    dimens: Dimens = DefaultDimens,
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = CornellRed,
            darkIcons = false
        )
    }

    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
