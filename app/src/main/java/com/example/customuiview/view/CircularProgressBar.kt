package com.example.customuiview.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularProgressBar(
    animationPlayer: Boolean = false,
    percentage: Float = 1f,
    number: Int = 100,
    radius: Dp = 50.dp,
    color: Color = Color.Green,
    width: Float = 10f,
    fontSize: TextUnit = 16.sp,
    durationMillis: Int = 1000,
) {

    val currentPercent by animateFloatAsState(
        targetValue = if (animationPlayer) percentage else 0f,
        animationSpec = tween(durationMillis = durationMillis, delayMillis = 0)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * currentPercent,
                useCenter = false,
                style = Stroke(width = width, cap = StrokeCap.Round),
            )
        }
        Text(
            text = (currentPercent * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
        )
    }

}
