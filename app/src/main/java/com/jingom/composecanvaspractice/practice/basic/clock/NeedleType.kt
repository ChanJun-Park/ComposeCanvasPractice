package com.jingom.composecanvaspractice.practice.basic.clock

sealed class NeedleType {
	object Hour: NeedleType()
	object Minute: NeedleType()
	object Second: NeedleType()
}
