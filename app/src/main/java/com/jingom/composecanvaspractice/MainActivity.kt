package com.jingom.composecanvaspractice

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.composecanvaspractice.ui.theme.ComposeCanvasPracticeTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MyCanvas()

//			Box(
//				modifier = Modifier
//					.fillMaxSize()
//					.drawBehind {
//
//					}
//			) {
//
//			}
		}
	}
}

@Composable
fun MyCanvas() {
	Canvas(
		modifier = Modifier
			.padding(30.dp)
			.size(500.dp)
	) {
		drawRect(
			color = Color.Black,
			size = size
		)

		for (i in 1..20) {
			drawIntoCanvas {
				it.nativeCanvas.apply {
					drawText(
						"${i * 25}dp",
						(i * 25).dp.toPx(),
						0f,
						Paint().apply {
							textSize = 20f
							color = Color.Red.toArgb()
							textAlign = Paint.Align.CENTER
						}
					)

					drawText(
						"${i * 25}dp",
						0f,
						(i * 25).dp.toPx(),
						Paint().apply {
							textSize = 20f
							color = Color.Red.toArgb()
							textAlign = Paint.Align.CENTER
						}
					)
				}
			}

			drawLine(
				color = Color.Red,
				start = Offset((i * 25).dp.toPx(), 0f),
				end = Offset((i * 25).dp.toPx(), size.height)
			)

			drawLine(
				color = Color.Red,
				start = Offset(0f, (i * 25).dp.toPx()),
				end = Offset(size.height, (i * 25).dp.toPx())
			)
		}

		drawRect(
			color = Color.White,
			topLeft = Offset(25.dp.toPx(), 25f.dp.toPx()),
			size = Size(200.dp.toPx(), 100.dp.toPx()),
			style = Stroke(
				width = 3.dp.toPx()
			)
		)

		drawCircle(
			brush = Brush.radialGradient(
				colors = listOf(Color.Red, Color.Yellow),
				center = center,
				radius = 50.dp.toPx()
			),
			radius = 50.dp.toPx(),
			center = center
		)

		drawArc(
			color = Color.Magenta,
			startAngle = 0f,
			sweepAngle = 270f,
			useCenter = true,
			topLeft = Offset(50.dp.toPx(), 250.dp.toPx()),
			size = Size(100.dp.toPx(), 100.dp.toPx())
		)

		drawArc(
			color = Color.Magenta,
			startAngle = 0f,
			sweepAngle = 270f,
			useCenter = false,
			topLeft = Offset(50.dp.toPx(), 375.dp.toPx()),
			size = Size(100.dp.toPx(), 100.dp.toPx())
		)

		drawArc(
			color = Color.Magenta,
			startAngle = 0f,
			sweepAngle = 270f,
			useCenter = false,
			topLeft = Offset(175.dp.toPx(), 375.dp.toPx()),
			size = Size(100.dp.toPx(), 100.dp.toPx()),
			style = Stroke(
				width = 3.dp.toPx()
			)
		)

		drawOval(
			color = Color.Cyan,
			topLeft = Offset(275.dp.toPx(), 50.dp.toPx()),
			size = Size(50.dp.toPx(), 100.dp.toPx())
		)
	}
}