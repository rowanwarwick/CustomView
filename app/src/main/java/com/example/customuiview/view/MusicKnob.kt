package com.example.customuiview.view

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import com.example.customuiview.R
import kotlin.math.PI
import kotlin.math.atan2


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MusicKnob(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 25f,
    onValueChange: (Float) -> Unit,
) {
    var rotation by remember {
        mutableFloatStateOf(limitingAngle)
    }
    var touchX by remember {
        mutableFloatStateOf(0f)
    }
    var touchY by remember {
        mutableFloatStateOf(0f)
    }
    var centerX by remember {
        mutableFloatStateOf(0f)
    }
    var centerY by remember {
        mutableFloatStateOf(0f)
    }

    Image(
        painter = painterResource(id = R.drawable.music_knob),
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val windowBounds = it.boundsInWindow()
                centerX = windowBounds.size.width / 2f
                centerY = windowBounds.size.height / 2f
            }
            .pointerInteropFilter {
                touchX = it.x
                touchY = it.y
                val angle = -atan2(centerX - touchX, centerY - touchY) * (180 / PI).toFloat()
                val action = listOf(MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE)
                if (it.action in action && angle !in -limitingAngle..limitingAngle) {
                    val fixedAngle = if (angle in -180f..-limitingAngle) 360f + angle else angle
                    rotation = fixedAngle
                    val percent = (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
                    onValueChange(percent)
                    true
                } else {
                    false
                }
            }
            .rotate(rotation)
    )
}

@Composable
fun VolumeBar(
    modifier: Modifier = Modifier,
    activeBar: Int = 0,
    barCount: Int = 10,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val barWidth: Float by remember {
            mutableFloatStateOf(constraints.maxWidth / (2f * barCount))
        }
        Canvas(modifier = modifier) {
            (0 until barCount).forEachIndexed { index, _ ->
                drawRoundRect(
                    color = if (index in 0..activeBar) Color.Green else Color.DarkGray,
                    topLeft = Offset(index * barWidth * 2f + barWidth / 2f, 0f),
                    size = Size(barWidth, constraints.maxHeight.toFloat())
                )
            }
        }
    }
}