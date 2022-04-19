package com.jingom.composecanvaspractice.practice.image

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.jingom.composecanvaspractice.R

@Composable
fun ImagePractice() {
	val kermitImage = ImageBitmap.imageResource(id = R.drawable.kermit)
	Canvas(
		modifier = Modifier.fillMaxSize()
	) {
		drawImage(
			image = kermitImage,
			dstOffset = IntOffset(100, 100),
			dstSize = IntSize(
				width = (400 * (kermitImage.width.toFloat() / kermitImage.height)).toInt(),
				height = 400
			)
		)
		drawCircle(
			color = Color.Red,
			radius = 200f,
			center = Offset(300f, 300f),
			blendMode = BlendMode.Exclusion
		)
	}
}