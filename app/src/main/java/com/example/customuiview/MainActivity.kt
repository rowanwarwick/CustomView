package com.example.customuiview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.customuiview.ui.theme.CustomUiViewTheme
import com.example.customuiview.view.CircularProgressBar
import com.example.customuiview.view.MusicKnob
import com.example.customuiview.view.Timer
import com.example.customuiview.view.VolumeBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomUiViewTheme {
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
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black)
                                .padding(16.dp),
                        ) {
                            Timer(
                                totalTime = 3L * 1000L,
                                handleColor = Color.Green,
                                inactiveBarColor = Color.DarkGray,
                                activeBar = Color(0xFF378802),
                                modifier = Modifier.size(200.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
