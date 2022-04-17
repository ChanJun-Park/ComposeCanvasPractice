package com.jingom.composecanvaspractice.practice.basic

sealed class LineType {
	object Normal: LineType()
	object FiveStep: LineType()
	object TenStep: LineType()
}
