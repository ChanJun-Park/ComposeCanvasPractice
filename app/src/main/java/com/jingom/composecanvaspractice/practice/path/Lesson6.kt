package com.jingom.composecanvaspractice.practice.path

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun PathTextExample() {
	Canvas(modifier = Modifier.fillMaxSize()) {

		val path = Path().apply {
			moveTo(200f, 800f)
			quadTo(600f, 400f, 1000f, 800f)
		}
		drawContext.canvas.nativeCanvas.apply {
			drawTextOnPath(
				"Hello, World",
				path,
				30f,
				30f,
				Paint().apply {
					color = Color.RED
					textAlign = Paint.Align.CENTER
					textSize = 40f
				}
			)
		}
	}
}