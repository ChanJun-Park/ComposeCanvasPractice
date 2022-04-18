package com.jingom.composecanvaspractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jingom.composecanvaspractice.practice.basic.clock.Clock
import com.jingom.composecanvaspractice.practice.path.*
import com.jingom.composecanvaspractice.practice.path.genderpicker.GenderPicker
import com.jingom.composecanvaspractice.practice.path.tictactoe.Player
import com.jingom.composecanvaspractice.practice.path.tictactoe.TicTacToe

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			Column(
				modifier = Modifier
					.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				var winner by remember {
					mutableStateOf<Player?>(null)
				}

				TicTacToe(
					modifier = Modifier
						.fillMaxWidth()
						.height(300.dp)
						.padding(30.dp),
					onPlayerWin = { gameWinner ->
						winner = gameWinner
					},
					onNewGame = {
						winner = null
					}
				)

				Text(
					modifier = Modifier
						.fillMaxWidth()
						.height(100.dp),
					text = winner?.let {
						"${it.javaClass.simpleName} is win"
					} ?: ""
				)
			}
		}
	}
}