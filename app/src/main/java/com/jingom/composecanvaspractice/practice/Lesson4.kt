package com.jingom.composecanvaspractice.practice

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withRotation
import kotlin.math.*

@Composable
fun Scale(
	modifier: Modifier = Modifier,
	scaleStyle: ScaleStyle = ScaleStyle(),
	minWeight: Int = 20,
	maxWeight: Int = 250,
	initialWeight: Int = 80,
	onWeightChange: (Int) -> Unit = {}
) {
	val radius = scaleStyle.radius
	val scaleWidth = scaleStyle.scaleWidth
	var center by remember {
		mutableStateOf(Offset.Zero)
	}
	var circleCenter by remember {
		mutableStateOf(Offset.Zero)
	}
	var angle by remember {
		mutableStateOf(0f)
	}
	var dragStartAngle by remember {
		mutableStateOf(0f)
	}
	var oldAngle by remember {
		mutableStateOf(angle)
	}

	Canvas(
		modifier = modifier
			.pointerInput(true) {
				detectDragGestures(
					onDragStart = { offset ->
						dragStartAngle = -atan2(
							circleCenter.x - offset.x,
							circleCenter.y - offset.y
						) * (180 / PI).toFloat()
					},
					onDragEnd = {
						oldAngle = angle
					}
				) { change, _ ->
					val touchAngle = -atan2(
						circleCenter.x - change.position.x,
						circleCenter.y - change.position.y
					) * (180 / PI).toFloat()

					val newAngle = oldAngle + (touchAngle - dragStartAngle)
					angle = newAngle.coerceIn(
						minimumValue = initialWeight - maxWeight.toFloat(),
						maximumValue = initialWeight - minWeight.toFloat()
					)
					onWeightChange((initialWeight - angle).roundToInt())
				}
			}
	) {
		drawRect(
			color = androidx.compose.ui.graphics.Color.Red,
			topLeft = Offset(
				x = 0f,
				y = 0f
			),
			size = this.size
		)

		center = this.center
		circleCenter = Offset(
			x = center.x,
			y = center.y + scaleWidth.toPx() / 2 + radius.toPx()
		)

		val outerRadius = radius.toPx() + scaleWidth.toPx() / 2f
		val innerRadius = radius.toPx() - scaleWidth.toPx() / 2f

		drawContext.canvas.nativeCanvas.apply {
			drawCircle(
				circleCenter.x,
				circleCenter.y,
				radius.toPx(),
				Paint().apply {
					isAntiAlias = true
					color = Color.WHITE
					strokeWidth = scaleWidth.toPx()
					style = Paint.Style.STROKE
					setShadowLayer(
						60f,
						0f,
						0f,
						Color.argb(50, 0, 0, 0)
					)
				}
			)
		}

		// Draw Lines
		for (index in minWeight..maxWeight) {
			val angleInRad = (index - initialWeight + angle - 90) * (PI / 180).toFloat()
			val lineType = when {
				index % 10 == 0 -> LineType.TenStep
				index % 5 == 0 -> LineType.FiveStep
				else -> LineType.Normal
			}
			val lineLength = when(lineType) {
				LineType.Normal -> scaleStyle.normalLineLength.toPx()
				LineType.FiveStep -> scaleStyle.fiveStepLineLength.toPx()
				LineType.TenStep -> scaleStyle.tenStepLineLength.toPx()
			}
			val lineColor = when(lineType) {
				LineType.Normal -> scaleStyle.normalLineColor
				LineType.FiveStep -> scaleStyle.fiveStepLineColor
				LineType.TenStep -> scaleStyle.tenStepLineColor
			}
			val lineStart = Offset(
				x = (outerRadius - lineLength) * cos(angleInRad) + circleCenter.x,
				y = (outerRadius - lineLength) * sin(angleInRad) + circleCenter.y,
			)
			val lineEnd = Offset(
				x = outerRadius * cos(angleInRad) + circleCenter.x,
				y = outerRadius * sin(angleInRad) + circleCenter.y,
			)

			drawContext.canvas.nativeCanvas.apply {
				if (lineType == LineType.TenStep) {
					val textRadius = (outerRadius - lineLength - 5.dp.toPx() - scaleStyle.textSize.toPx())
					val x = textRadius * cos(angleInRad) + circleCenter.x
					val y = textRadius * sin(angleInRad) + circleCenter.y

					withRotation(
						degrees = angleInRad * (180f / PI).toFloat() + 90,
						pivotX = x,
						pivotY = y
					) {
						drawText(
							abs(index).toString(),
							x,
							y,
							Paint().apply {
								isAntiAlias = true
								color = Color.BLACK
								textSize = scaleStyle.textSize.toPx()
								textAlign = Paint.Align.CENTER
							}
						)
					}
				}
			}
			drawLine(
				color = lineColor,
				start = lineStart,
				end = lineEnd,
				strokeWidth = 1.dp.toPx()
			)
			val middleTop = Offset(
				x = circleCenter.x,
				y = circleCenter.y - innerRadius - scaleStyle.scaleIndicatorLength.toPx()
			)
			val bottomLeft = Offset(
				x = circleCenter.x - 3f,
				y = circleCenter.y - innerRadius
			)
			val bottomRight = Offset(
				x = circleCenter.x + 3f,
				y = circleCenter.y - innerRadius
			)
			val indicator = Path().apply {
				moveTo(middleTop.x, middleTop.y)
				lineTo(bottomLeft.x, bottomLeft.y)
				lineTo(bottomRight.x, bottomRight.y)
				close()
			}
			drawPath(
				path = indicator,
				color = scaleStyle.scaleIndicatorColor
			)
		}
	}
}