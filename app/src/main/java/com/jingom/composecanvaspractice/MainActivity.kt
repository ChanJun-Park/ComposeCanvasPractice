package com.jingom.composecanvaspractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jingom.composecanvaspractice.practice.image.imagereveal.ImageReveal

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ImageReveal()
		}
	}
}