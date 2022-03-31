package com.jingom.composecanvaspractice.practice.path

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StampedPathEffectStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun PathEffectPractice() {
	val infiniteTransition = rememberInfiniteTransition()
	val phase by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 10000f,
		animationSpec = infiniteRepeatable(
			animation = tween(
				durationMillis = 60000,
				delayMillis = 0,
				easing = LinearEasing
			)
		)
	)

	Canvas(modifier = Modifier.fillMaxSize()) {
		val path = Path().apply {
			moveTo(100f, 100f)
			cubicTo(300f, 800f, 400f, 800f, 800f, 1000f)
			lineTo(400f, 1200f)
			lineTo(700f, 1800f)
		}

//		drawPath(
//			path = path,
//			color = Color.Red,
//			style = Stroke(
//				width = 3.dp.toPx(),
//				pathEffect = PathEffect.dashPathEffect(
//					intervals = floatArrayOf(30f, 30f),
//					phase = phase
//				)
//			)
//		)

//		drawPath(
//			path = path,
//			color = Color.Red,
//			style = Stroke(
//				width = 3.dp.toPx(),
//				pathEffect = PathEffect.cornerPathEffect(
//					radius = 100f
//				)
//			)
//		)

		val oval = Path().apply {
			addOval(Rect(Offset.Zero, Offset(40f, 10f)))
		}

//		drawPath(
//			path = path,
//			color = Color.Red,
//			style = Stroke(
//				width = 3.dp.toPx(),
//				pathEffect = PathEffect.stampedPathEffect(
//					shape = oval,
//					advance = 100f,
//					phase = phase,
//					style = StampedPathEffectStyle.Rotate
//				)
//			)
//		)

//		drawPath(
//			path = path,
//			color = Color.Red,
//			style = Stroke(
//				width = 3.dp.toPx(),
//				pathEffect = PathEffect.stampedPathEffect(
//					shape = oval,
//					advance = 100f,
//					phase = phase,
//					style = StampedPathEffectStyle.Morph
//				)
//			)
//		)

//		drawPath(
//			path = path,
//			color = Color.Red,
//			style = Stroke(
//				width = 3.dp.toPx(),
//				pathEffect = PathEffect.stampedPathEffect(
//					shape = oval,
//					advance = 100f,
//					phase = phase,
//					style = StampedPathEffectStyle.Translate
//				)
//			)
//		)

		drawPath(
			path = path,
			color = Color.Red,
			style = Stroke(
				width = 3.dp.toPx(),
				pathEffect = PathEffect.chainPathEffect(
					outer = PathEffect.stampedPathEffect(
						shape = oval,
						advance = 30f,
						phase = 0f,
						style = StampedPathEffectStyle.Morph
					),
					inner = PathEffect.dashPathEffect(
						intervals = floatArrayOf(100f, 100f),
					)
				)
			)
		)
	}
}