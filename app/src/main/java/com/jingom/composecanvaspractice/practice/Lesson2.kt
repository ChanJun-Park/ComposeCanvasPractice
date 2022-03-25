package com.jingom.composecanvaspractice.practice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun BallTrackerGame() {
	var isPlaying by remember {
		mutableStateOf(false)
	}

	var isStarted by remember {
		mutableStateOf(false)
	}

	var point by remember {
		mutableStateOf(0)
	}

	Column(modifier = Modifier.fillMaxSize()) {
		HeaderRow(
			point = point,
			isStarted = isStarted,
			isPlaying = isPlaying,
			onButtonClicked = {
				if (isStarted) {
					isStarted = false
					isPlaying = false
					point = 0
				} else {
					isStarted = true
					isPlaying = true
				}
			},
			onFinished = {
				isPlaying = false
			}
		)
		BallTracker(
			isPlaying = isPlaying
		) {
			point += 1
		}
	}
}

@Composable
fun HeaderRow(
	point: Int,
	isStarted: Boolean = false,
	isPlaying: Boolean = false,
	onButtonClicked: () -> Unit = {},
	onFinished: () -> Unit = {}
) {
	Row(
		modifier = Modifier
			.padding(10.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Text(text = "point: $point")

		Button(onClick = onButtonClicked) {
			Text(text = if (isStarted) "reset" else "start")
		}

		CountDownTimer(
			isPlaying = isPlaying,
			onFinished = onFinished
		)
	}
}

@Composable
fun CountDownTimer(
	gameTime: Long = 30000L,
	isPlaying: Boolean = false,
	onFinished: () -> Unit = {},
) {
	var time by remember(key1 = isPlaying) {
		mutableStateOf(gameTime)
	}

	LaunchedEffect(key1 = isPlaying, key2 = time) {
		if (!isPlaying) {
			return@LaunchedEffect
		}

		if (time > 0) {
			delay(1000)
			time -= 1000
		} else {
			onFinished()
		}
	}

	Text(text = "${time / 1000}")
}

@Composable
fun BallTracker(
	radius: Float = 100f,
	isPlaying: Boolean = false,
	onTouch: () -> Unit = {}
) {
	BoxWithConstraints(
		modifier = Modifier
			.fillMaxSize()
	) {
		var ballPosition by remember {
			mutableStateOf(getRandomOffset(radius, constraints.maxWidth, constraints.maxHeight))
		}

		Canvas(
			modifier = Modifier
				.fillMaxSize()
				.pointerInput(key1 = isPlaying) {
					if (!isPlaying) {
						return@pointerInput
					}

					detectTapGestures {
						val distance = sqrt((ballPosition.x - it.x).pow(2) + (ballPosition.y - it.y).pow(2))
						if (distance < radius) {
							ballPosition = getRandomOffset(radius, constraints.maxWidth, constraints.maxHeight)
							onTouch()
						}
					}
				}
		) {
			drawCircle(
				color = Color.Red,
				radius = radius,
				center = ballPosition,
			)
		}
	}
}

private fun getRandomOffset(radius: Float, width: Int, height: Int): Offset {
	return Offset(
		x = Random.nextInt(radius.roundToInt(), width - radius.roundToInt()).toFloat(),
		y = Random.nextInt(radius.roundToInt(), height - radius.roundToInt()).toFloat()
	)
}