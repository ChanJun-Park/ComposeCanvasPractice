package com.jingom.composecanvaspractice.practice.path

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun PathAnimation() {

	val pathAnimation = remember {
		Animatable(0f)
	}
	val path = Path().apply {
		moveTo(100f, 100f)
		quadraticBezierTo(100f, 400f, 400f, 400f)
	}
	val outPath = Path()
	PathMeasure().apply {
		setPath(path, false)
		getSegment(0f, pathAnimation.value * length, outPath)
	}
	LaunchedEffect(key1 = true) {
		pathAnimation.animateTo(
			targetValue = 1f,
			animationSpec = tween(
				durationMillis = 1000
			)
		)
	}
	Canvas(modifier = Modifier.fillMaxSize()) {
		drawPath(
			path = outPath,
			color = Color.Red,
			style = Stroke(width = 2.dp.toPx())
		)
	}
}