package com.jingom.composecanvaspractice.practice.basic.clock

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.delay
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Clock(
	modifier: Modifier = Modifier,
	clockStyle: ClockStyle = ClockStyle()
) {
	var currentTime by remember {
		mutableStateOf(LocalTime.now())
	}

	LaunchedEffect(key1 = true) {
		while (true) {
			delay(1000)
			currentTime = LocalTime.now()
		}
	}

	Canvas(modifier = modifier) {
		drawContext.canvas.nativeCanvas.apply {
			drawCircle(
				center.x,
				center.y,
				clockStyle.clockRadius.toPx(),
				Paint().apply {
					color = clockStyle.clockBackgroundColor.toArgb()
					setShadowLayer(
						30f,
						0f,
						0f,
						Color.LightGray.toArgb()
					)
				}
			)
		}

		for (minute in 0..59) {
			val lineType = when {
				minute % 5 == 0 -> LineType.FiveStep
				else -> LineType.Normal
			}
			val lineLength = when (lineType) {
				LineType.FiveStep -> clockStyle.fiveStepLength
				LineType.Normal -> clockStyle.normalStepLength
			}
			val lineColor = when (lineType) {
				LineType.FiveStep -> clockStyle.fiveStepColor
				LineType.Normal -> clockStyle.normalStepColor
			}

			val angle = (360 / 60) * minute - 90
			val angleInRad = angle * (PI / 180).toFloat()

			val lineStart = Offset(
				x = cos(angleInRad) * clockStyle.clockRadius.toPx() + center.x,
				y = sin(angleInRad) * clockStyle.clockRadius.toPx() + center.y
			)
			val lineEnd = Offset(
				x = cos(angleInRad) * (clockStyle.clockRadius - lineLength).toPx() + center.x,
				y = sin(angleInRad) * (clockStyle.clockRadius - lineLength).toPx() + center.y
			)

			drawLine(
				color = lineColor,
				start = lineStart,
				end = lineEnd
			)
		}

		drawNeedle(NeedleType.Hour, clockStyle.hourNeedleStyle, currentTime)
		drawNeedle(NeedleType.Minute, clockStyle.minuteNeedleStyle, currentTime)
		drawNeedle(NeedleType.Second, clockStyle.secondNeedleStyle, currentTime)
	}
}

private fun DrawScope.drawNeedle(needleType: NeedleType, needleStyle: NeedleStyle, currentTime: LocalTime) {
	needleStyle.apply {
		val angleInRad = getNeedleAngleInRad(needleType, currentTime)

		val lineStart = center
		val lineEnd = Offset(
			x = cos(angleInRad) * needleLength.toPx() + center.x,
			y = sin(angleInRad) * needleLength.toPx() + center.y
		)

		drawLine(
			color = needleColor,
			start = lineStart,
			end = lineEnd,
			strokeWidth = needleWidth.toPx(),
			cap = StrokeCap.Round
		)
	}
}

private fun getNeedleAngleInRad(needleType: NeedleType, currentTime: LocalTime): Float {
	return when (needleType) {
		NeedleType.Hour -> {
			val totalSecondInDay = 24 * 3600
			val currentSecondInDay = currentTime.toSecondOfDay()

			val angle = (360f / totalSecondInDay) * currentSecondInDay * 2 - 90
			angle * (PI / 180).toFloat()
		}
		NeedleType.Minute -> {
			val totalSecondInHour = 3600
			val currentSecondInHour = currentTime.toSecondOfDay() % 3600

			val angle = (360f / totalSecondInHour) * currentSecondInHour - 90
			angle * (PI / 180).toFloat()
		}
		NeedleType.Second -> {
			val angle = (360f / 60) * currentTime.second - 90
			angle * (PI / 180).toFloat()
		}
	}
}