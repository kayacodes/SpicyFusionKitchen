@file:Suppress("MagicNumber")

package com.sfk.app.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Black = Color(0xFF000000)
val Timberwolf = Color(0xFFDED4CF)
val BabyPowder = Color(0xFFFCFCF9)
val Asparagus = Color(0xFF7A9863)
val CornellRed = Color(0xFFC6021A)
val ForestGreen = Color(0xFF245343)
data class SfkBottomBarColors(
    val container: Color,
    val liquid: Color,
    val icon: Color,
    val selectedIcon: Color
)

object SfkBottomBarDefaults {
    val Elevation: Dp = 12.dp
    fun colors(
        container: Color = Color.Black,
        liquid: Color = Color(0xFF333333),
        icon: Color = Color(0xFFFCFCF9),
        selectedIcon: Color = Color.White
    ) = SfkBottomBarColors(container, liquid, icon, selectedIcon)
}

data class SfkBottomBarStyle(
    val colors: SfkBottomBarColors = SfkBottomBarDefaults.colors(),
    val elevation: Dp = SfkBottomBarDefaults.Elevation
)
