package com.sfk.app.ui.components.bottombar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SfkBottomBarState internal constructor(tabCount: Int) {
    val startX = Animatable(0f)
    val endX = Animatable(0f)
    val centers = mutableStateListOf<Float>().apply { repeat(tabCount) { add(0f) } }
    var barWidthPx by mutableFloatStateOf(0f)
        internal set

    suspend fun animateTo(index: Int) {
        if (centers.isEmpty()) return
        val target = centers[index]
        if (startX.value == 0f && endX.value == 0f) {
            startX.snapTo(target)
            endX.snapTo(target)
            return
        }
        val goingRight = target > endX.value
        val stretch = tween<Float>(
            durationMillis = STRETCH_DURATION_MS,
            easing = FastOutSlowInEasing
        )
        val catchUp = tween<Float>(
            durationMillis = STRETCH_DURATION_MS - LAG_MS,
            delayMillis = LAG_MS,
            easing = LinearOutSlowInEasing
        )
        coroutineScope {
            if (goingRight) {
                launch { endX.animateTo(target, stretch) }
                launch { startX.animateTo(target, catchUp) }
            } else {
                launch { startX.animateTo(target, stretch) }
                launch { endX.animateTo(target, catchUp) }
            }
        }
    }

    companion object {
        val BAR_CORNER_RADIUS_DP = 28.dp
        val BAR_PADDING_H_DP = 18.dp
        val BAR_PADDING_V_DP = 10.dp
        val LIQUID_HEIGHT_DP = 44.dp
        val ICON_BOX_DP = 44.dp
        val LIQUID_RADIUS_DP = 18.dp
        const val SELECTED_ICON_SCALE = 1.06f
        const val BASE_ICON_SCALE = 1f
        const val ICON_DAMPING = 0.75f
        const val HALF = 0.5f
        const val PINCH_FACTOR_MAX = 1.4f
        const val MIN_DIST_PX = 1f
        const val STRETCH_DURATION_MS = 600
        const val LAG_MS = 120
    }
}

@Composable
fun rememberSfkBottomBarState(
    tabs: List<BottomTab>,
    selectedRoute: String
): SfkBottomBarState {
    val selectedIndex = tabs.indexOfFirst { it.route == selectedRoute }.coerceAtLeast(0)
    val state = remember(tabs.size) { SfkBottomBarState(tabs.size) }
    LaunchedEffect(selectedIndex, state.centers.toList(), state.barWidthPx) {
        state.animateTo(selectedIndex)
    }
    return state
}
