package com.jingom.composecanvaspractice.practice.path

import android.graphics.PathMeasure
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.atan2

@Composable
fun PathAnimation() {

	val pathAnimation = remember {
		Animatable(0f)
	}

	val path = Path().apply {
		moveTo(100f, 100f)
		quadraticBezierTo(100f, 400f, 400f, 400f)
	}
	val outPath = android.graphics.Path()

	val pos = FloatArray(2)
	val tan = FloatArray(2)

	PathMeasure().apply {
		setPath(path.asAndroidPath(), false)
		getSegment(0f, pathAnimation.value * length, outPath, true)
		getPosTan(pathAnimation.value * length, pos, tan)
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
			path = outPath.asComposePath(),
			color = Color.Red,
			style = Stroke(width = 2.dp.toPx())
		)
		val x = pos[0]
		val y = pos[1]

		val angle = (-atan2(tan[0], tan[1]) * (180 / PI)).toFloat()
		rotate(angle, Offset(x, y)) {
			drawPath(
				path = Path().apply {
					moveTo(x, y + 60f)
					lineTo(x + 30f, y - 30f)
					lineTo(x - 30f, y - 30f)
					close()
				},
				color = Color.Red
			)
		}
	}
}