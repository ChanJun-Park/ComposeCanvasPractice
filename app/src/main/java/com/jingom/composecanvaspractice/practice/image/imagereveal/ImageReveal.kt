package com.jingom.composecanvaspractice.practice.image.imagereveal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.jingom.composecanvaspractice.R

@Composable
fun ImageReveal() {
	val kermitImage = ImageBitmap.imageResource(id = R.drawable.kermit)

	var touchOffset by remember {
		mutableStateOf(Offset.Zero)
	}

	var isDragging by remember {
		mutableStateOf(false)
	}

	Canvas(
		modifier = Modifier
			.fillMaxSize()
			.pointerInput(true) {
				detectDragGestures(
					onDragStart = {
						isDragging = true
						touchOffset = it
					},
					onDragEnd = {
						isDragging = false
					}
				) { change, _ ->
					touchOffset = change.position
				}
			}
	) {
		val width: Float
		val height: Float
		if (size.width == size.minDimension) {
			width = size.width
			height = width * (kermitImage.height.toFloat() / kermitImage.width)
		} else {
			height = size.height
			width = height * (kermitImage.width.toFloat() / kermitImage.height)
		}

		val circlePath = Path().apply{
			addArc(
				oval = Rect(
					center = touchOffset,
					radius = 200f
				),
				startAngleDegrees = 0f,
				sweepAngleDegrees = 306f
			)
		}

		drawImage(
			image = kermitImage,
			dstOffset = IntOffset(0, (center.y - height / 2).toInt()),
			dstSize = IntSize(width.toInt(), height.toInt()),
			colorFilter = ColorFilter.tint(
				color = Color.Black,
				blendMode = BlendMode.Color
			)
		)

		if (isDragging) {
			clipPath(path = circlePath) {
				drawImage(
					image = kermitImage,
					dstOffset = IntOffset(0, (center.y - height / 2).toInt()),
					dstSize = IntSize(width.toInt(), height.toInt())
				)
			}
		}
	}
}