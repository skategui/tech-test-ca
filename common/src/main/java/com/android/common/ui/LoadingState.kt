package com.android.common.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


@Preview
@Composable
fun LoadingStatePreview() {
    LoadingState(
        modifier = Modifier,
        message = "Loading in progress")
}

/**
 * Display a loader full screen
 */
@Composable
fun LoadingState(modifier: Modifier = Modifier, message : String) {
    Surface(
        color = Color.White.copy(alpha = 0.98f),
        modifier = modifier
            .fillMaxSize()
            .zIndex(10f)) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SpinningProgressBar()
            Text(
                text = message,
                fontSize = 26.sp,
                modifier = modifier
                    .padding(top = 40.dp)
            )
        }
    }
}

@Composable
fun SpinningProgressBar(modifier: Modifier = Modifier) {

    val count = 8

    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = count.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(count * 80, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.size(42.dp)) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        val width = size.width * .3f
        val height = size.height / 9

        val cornerRadius = width.coerceAtMost(height) / 2

        for (i in 0..360 step 360 / count) {
            rotate(i.toFloat()) {
                drawRoundRect(
                    color = Color.LightGray.copy(alpha = .7f),
                    topLeft = Offset(canvasWidth - width, (canvasHeight - height) / 2),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
        }

        val coefficient = 360f / count

        for (i in 1..4) {
            rotate((angle.toInt() + i) * coefficient) {
                drawRoundRect(
                    color = Color.Gray.copy(alpha = (0.2f + 0.2f * i).coerceIn(0f, 1f)),
                    topLeft = Offset(canvasWidth - width, (canvasHeight - height) / 2),
                    size = Size(width, height),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
        }
    }
}



