package com.sfk.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Space(
    val xs: Dp = 4.dp,
    val s: Dp = 8.dp,
    val m: Dp = 12.dp,
    val l: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp
)

@Immutable
data class Radius(
    val s: Dp = 8.dp,
    val m: Dp = 12.dp,
    val l: Dp = 16.dp,
    val xl: Dp = 28.dp
)

@Immutable
data class Size(
    val iconS: Dp = 20.dp,
    val iconM: Dp = 24.dp,
    val iconL: Dp = 28.dp,
    val chipH: Dp = 56.dp,
    val searchH: Dp = 48.dp,
    val cardImageH: Dp = 160.dp,
    val gridGap: Dp = 12.dp,
    val bottomBarH: Dp = 72.dp
)

@Immutable
data class Elevation(
    val bar: Dp = 12.dp,
    val card: Dp = 0.dp
)

@Stable
data class Dimens(
    val space: Space = Space(),
    val radius: Radius = Radius(),
    val size: Size = Size(),
    val elevation: Elevation = Elevation()
)

val DefaultDimens = Dimens()

val LocalDimens: ProvidableCompositionLocal<Dimens> =
    staticCompositionLocalOf { DefaultDimens }

@Composable
fun ProvideDimens(dimens: Dimens = DefaultDimens, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDimens provides dimens, content = content)
}
