package com.jingom.composecanvaspractice.practice

sealed class LineType {
	object Normal: LineType()
	object FiveStep: LineType()
	object TenStep: LineType()
}
