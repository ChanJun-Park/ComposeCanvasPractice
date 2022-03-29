package com.jingom.composecanvaspractice.practice.path

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.unit.dp

@Composable
fun PathPractice() {
	Canvas(modifier = Modifier.fillMaxSize()) {
		translate(left = 200f, top = 200f) {
			rotate(45f, Offset(200f, 200f)) {

			}
		}
		scale(0.5f, Offset(200f, 200f)) {

		}
		val path = Path().apply {
			addOval(
				oval = Rect(
					center = Offset(400f, 400f),
					radius = 200f
				)
			)
		}
		drawPath(
			path = path,
			color = Color.Black,
			style = Stroke(width = 2.dp.toPx())
		)
		clipPath(
			path = path
		) {
			drawRect(
				color = Color.Red,
				topLeft = Offset(400f, 400f),
				size = Size(400f, 400f)
			)
		}
	}
}