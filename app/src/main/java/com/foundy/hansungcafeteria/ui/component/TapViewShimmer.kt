package com.foundy.hansungcafeteria.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TapViewShimmer() {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Reverse,
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )

    ShimmerTabView(brush = brush)
}

@Composable
private fun ShimmerTabView(brush: Brush) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ShimmerHeadlineText(brush = brush)
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            ShimmerCardItem(brush = brush)
            ShimmerCardItem(brush = brush)
        }
    }
}

@Composable
private fun ShimmerHeadlineText(brush: Brush) {
    Column {
        Spacer(
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Spacer(
            modifier = Modifier
                .size(width = 120.dp, height = 16.dp)
                .clip(CircleShape)
                .background(brush)
        )
        Spacer(
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

@Composable
private fun ShimmerCardItem(brush: Brush) {
    HansungCard {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .size(width = 200.dp, height = 32.dp)
                    .clip(CircleShape)
                    .background(brush)
            )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
            Column {
                for (i in 1..10) {
                    Spacer(
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Spacer(
                        modifier = Modifier
                            .size(width = 120.dp, height = 16.dp)
                            .clip(CircleShape)
                            .background(brush)
                    )
                    Spacer(
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ShimmerTabViewPreview() {
    ShimmerCardItem(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            )
        )
    )
}

@Composable
@Preview(showBackground = true)
fun ShimmerCardItemPreview() {
    ShimmerCardItem(
        brush = Brush.linearGradient(
            listOf(
                Color.LightGray.copy(alpha = 0.6f),
                Color.LightGray.copy(alpha = 0.2f),
                Color.LightGray.copy(alpha = 0.6f),
            )
        )
    )
}