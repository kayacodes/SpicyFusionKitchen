package com.sfk.app.ui.components.bottombar

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.sfk.app.ui.theme.SfkBottomBarStyle

@SuppressLint("ComposableNaming")
@Composable
fun sfkBottomBar(
    tabs: List<BottomTab>,
    selectedRoute: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: SfkBottomBarStyle = SfkBottomBarStyle()
) {
    val density = LocalDensity.current
    val rPx = with(density) { SfkBottomBarState.LIQUID_RADIUS_DP.toPx() }
    val state = rememberSfkBottomBarState(tabs, selectedRoute)
    val selectedIndex = tabs.indexOfFirst { it.route == selectedRoute }.coerceAtLeast(0)

    Surface(
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SfkBottomBarState.BAR_PADDING_H_DP, vertical = SfkBottomBarState.BAR_PADDING_V_DP)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    style.elevation,
                    RoundedCornerShape(SfkBottomBarState.BAR_CORNER_RADIUS_DP),
                    clip = false
                )
                .background(
                    style.colors.container,
                    RoundedCornerShape(SfkBottomBarState.BAR_CORNER_RADIUS_DP)
                )
                .padding(
                    horizontal = SfkBottomBarState.BAR_PADDING_H_DP,
                    vertical = SfkBottomBarState.BAR_PADDING_V_DP
                )
                .fillMaxWidth()
                .onGloballyPositioned { state.barWidthPx = it.size.width.toFloat() }
        ) {
            liquidHighlight(state = state, rPx = rPx, color = style.colors.liquid)
            tabsRow(
                tabs = tabs,
                selectedIndex = selectedIndex,
                style = style,
                onTabSelected = onTabSelected
            ) { index, cx ->
                if (state.centers.size > index) state.centers[index] = cx
            }
        }
    }
}

@Composable
private fun liquidHighlight(
    state: SfkBottomBarState,
    rPx: Float,
    color: Color
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(SfkBottomBarState.LIQUID_HEIGHT_DP)
    ) {
        Canvas(
            Modifier
                .fillMaxWidth()
                .height(SfkBottomBarState.LIQUID_HEIGHT_DP)
        ) {
            val hCenter = size.height * SfkBottomBarState.HALF
            val sx = state.startX.value
            val ex = state.endX.value
            if (sx == 0f && ex == 0f) return@Canvas
            val left = kotlin.math.min(sx, ex)
            val right = kotlin.math.max(sx, ex)
            val dist = (right - left).coerceAtLeast(SfkBottomBarState.MIN_DIST_PX)
            val pinch = (dist * SfkBottomBarState.HALF)
                .coerceAtMost(rPx * SfkBottomBarState.PINCH_FACTOR_MAX)
            val topY = hCenter - rPx
            val botY = hCenter + rPx
            val midX = (left + right) * SfkBottomBarState.HALF
            val path = Path().apply {
                moveTo(left, topY)
                quadraticTo(midX, topY + pinch, right, topY)
                arcTo(
                    rect = Rect(
                        right - rPx,
                        hCenter - rPx,
                        right + rPx,
                        hCenter + rPx
                    ),
                    startAngleDegrees = -90f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                quadraticTo(midX, botY - pinch, left, botY)
                arcTo(
                    rect = Rect(
                        left - rPx,
                        hCenter - rPx,
                        left + rPx,
                        hCenter + rPx
                    ),
                    startAngleDegrees = 90f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                close()
            }
            drawPath(path = path, color = color, style = Fill)
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
private fun tabsRow(
    tabs: List<BottomTab>,
    selectedIndex: Int,
    style: SfkBottomBarStyle,
    onTabSelected: (String) -> Unit,
    onIconPositioned: (index: Int, centerX: Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = index == selectedIndex
            val interaction = MutableInteractionSource()
            val scale by animateFloatAsState(
                targetValue = if (isSelected) {
                    SfkBottomBarState.SELECTED_ICON_SCALE
                } else {
                    SfkBottomBarState.BASE_ICON_SCALE
                },
                animationSpec = spring(
                    dampingRatio = SfkBottomBarState.ICON_DAMPING,
                    stiffness = Spring.StiffnessLow
                ),
                label = "iconScale"
            )
            val tint = if (isSelected) style.colors.selectedIcon else style.colors.icon
            Box(
                modifier = Modifier
                    .size(SfkBottomBarState.ICON_BOX_DP)
                    .semantics { contentDescription = tab.contentDescription }
                    .clickable(
                        interactionSource = interaction,
                        indication = null
                    ) { onTabSelected(tab.route) }
                    .onGloballyPositioned { coords ->
                        onIconPositioned(index, coords.boundsInParent().center.x)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.scale(scale)
                )
            }
        }
    }
}
