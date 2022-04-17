package com.jingom.composecanvaspractice.practice.basic

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun TextTest() {
	Canvas(
		modifier = Modifier.fillMaxSize()
	) {
		drawContext.canvas.nativeCanvas.apply {
			drawText(
				"This is My Test",
				100f,
				200f,
				Paint().apply {
					isAntiAlias = true
					color = Color.RED
					textSize = 50f
				}
			)
		}
	}
}