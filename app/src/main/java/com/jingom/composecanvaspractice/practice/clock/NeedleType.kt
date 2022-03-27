package com.jingom.composecanvaspractice.practice.clock

sealed class NeedleType {
	object Hour: NeedleType()
	object Minute: NeedleType()
	object Second: NeedleType()
}
