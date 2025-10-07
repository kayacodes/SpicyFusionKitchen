package com.sfk.app.ui.components.bottombar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SfkBottomBar(
    tabs: List<BottomTab>,
    selectedRoute: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Black,
    liquidColor: Color = Color(0xFF333333),
    iconColor: Color = Color(0xFFFCFCF9),
    selectedIconColor: Color = Color(0xFFFFFFFF),
    elevation: Dp = 12.dp
) {
    val density = LocalDensity.current
    var barWidthPx by remember { mutableStateOf(0f) }
    val centers = remember { mutableStateListOf<Float>() }

    val selectedIndex = tabs.indexOfFirst { it.route == selectedRoute }.coerceAtLeast(0)

    val startX = remember { Animatable(0f) }
    val endX = remember { Animatable(0f) }

    val rDp = 18.dp
    val rPx = with(density) { rDp.toPx() }

    val stretchSpec = tween<Float>(600, easing = FastOutSlowInEasing)
    val catchUpSpec = tween<Float>(420, easing = LinearOutSlowInEasing)

    LaunchedEffect(tabs.size) {
        centers.clear()
        repeat(tabs.size) { centers += 0f }
    }

    LaunchedEffect(selectedIndex, centers.toList(), barWidthPx) {
        if (centers.isEmpty()) return@LaunchedEffect
        val target = centers[selectedIndex]
        if (startX.value == 0f && endX.value == 0f) {
            startX.snapTo(target)
            endX.snapTo(target)
        } else {
            val goingRight = target > endX.value
            if (goingRight) {
                launch { endX.animateTo(target, stretchSpec) }
                launch { startX.animateTo(target, catchUpSpec) }
            } else {
                launch { startX.animateTo(target, stretchSpec) }
                launch { endX.animateTo(target, catchUpSpec) }
            }
        }
    }

    Surface(
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Box(
            modifier = Modifier
                .shadow(elevation, RoundedCornerShape(28.dp), clip = false)
                .background(containerColor, RoundedCornerShape(28.dp))
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .fillMaxWidth()
                .onGloballyPositioned { barWidthPx = it.size.width.toFloat() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
            ) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    val hCenter = size.height / 2f
                    val sx = startX.value
                    val ex = endX.value
                    if (sx == 0f && ex == 0f) return@Canvas

                    val left = kotlin.math.min(sx, ex)
                    val right = kotlin.math.max(sx, ex)
                    val dist = (right - left).coerceAtLeast(1f)

                    val pinch = (dist / 2f).coerceAtMost(rPx * 1.4f)
                    val topY = hCenter - rPx
                    val botY = hCenter + rPx
                    val midX = (left + right) / 2f

                    val path = Path().apply {
                        moveTo(left, topY)
                        quadraticBezierTo(midX, topY + pinch, right, topY)
                        arcTo(
                            rect = Rect(
                                left = right - rPx,
                                top = hCenter - rPx,
                                right = right + rPx,
                                bottom = hCenter + rPx
                            ),
                            startAngleDegrees = -90f,
                            sweepAngleDegrees = 180f,
                            forceMoveTo = false
                        )
                        quadraticBezierTo(midX, botY - pinch, left, botY)
                        arcTo(
                            rect = Rect(
                                left = left - rPx,
                                top = hCenter - rPx,
                                right = left + rPx,
                                bottom = hCenter + rPx
                            ),
                            startAngleDegrees = 90f,
                            sweepAngleDegrees = 180f,
                            forceMoveTo = false
                        )
                        close()
                    }
                    drawPath(path = path, color = liquidColor, style = Fill)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = index == selectedIndex
                    val interaction = remember { MutableInteractionSource() }
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.06f else 1f,
                        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
                        label = "iconScale"
                    )
                    val tint = if (isSelected) selectedIconColor else iconColor

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .semantics { contentDescription = tab.contentDescription }
                            .clickable(
                                interactionSource = interaction,
                                indication = null
                            ) { onTabSelected(tab.route) }
                            .onGloballyPositioned { coords ->
                                val cx = coords.boundsInParent().center.x
                                if (centers.size > index) centers[index] = cx
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
    }
}
