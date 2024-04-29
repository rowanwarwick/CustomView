package com.example.customuiview

import android.os.Bundle
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.customuiview.ui.theme.CustomUiViewTheme
import kotlin.math.PI
import kotlin.math.atan2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomUiViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            var animationPlayer: Boolean by remember {
                                mutableStateOf(false)
                            }
                            CircularProgressBar(animationPlayer)
                            Button(
                                onClick = { animationPlayer = !animationPlayer },
                            ) {
                                Text(text = "Start progress bar")
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(16.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Black)
                                .border(1.dp, Color.Green)
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            var volume: Float by remember {
                                mutableFloatStateOf(0f)
                            }
                            val barCount = 20
                            MusicKnob(modifier = Modifier.size(100.dp)) { volume = it }
                            VolumeBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp),
                                activeBar = (barCount * volume).toInt(),
                                barCount = barCount
                            )
                        }

                    }

                }
            }
        }
    }
}

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
                val action = listOf(ACTION_DOWN, ACTION_MOVE)
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
            (0 until barCount).forEachIndexed { index, rect ->
                drawRoundRect(
                    color = if (index in 0..activeBar) Color.Green else Color.DarkGray,
                    topLeft = Offset(index * barWidth * 2f + barWidth / 2f, 0f),
                    size = Size(barWidth, constraints.maxHeight.toFloat())
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCircularProgressBar() {
    CircularProgressBar()
}