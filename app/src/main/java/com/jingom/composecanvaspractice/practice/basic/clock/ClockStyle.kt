package com.jingom.composecanvaspractice.practice.basic.clock

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ClockStyle(
	val clockRadius: Dp = 130.dp,
	val normalStepLength: Dp = 15.dp,
	val fiveStepLength: Dp = 20.dp,
	val normalStepColor: Color = Color.LightGray,
	val fiveStepColor: Color = Color.Black,
	val clockBackgroundColor: Color = Color.White,
	val hourNeedleStyle: NeedleStyle = NeedleStyle(80.dp, 3.dp, Color.Black),
	val minuteNeedleStyle: NeedleStyle = NeedleStyle(95.dp, 2.dp, Color.Black),
	val secondNeedleStyle: NeedleStyle = NeedleStyle(90.dp, 2.dp, Color.Red)
)

data class NeedleStyle(
	val needleLength: Dp,
	val needleWidth: Dp,
	val needleColor: Color
)