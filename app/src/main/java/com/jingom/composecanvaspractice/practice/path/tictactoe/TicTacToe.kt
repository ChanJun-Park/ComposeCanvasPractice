package com.jingom.composecanvaspractice.practice.path.tictactoe

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TicTacToe(
	modifier: Modifier = Modifier,
	onPlayerWin: (Player?) -> Unit,
	onNewGame: () -> Unit
) {
	val scope = rememberCoroutineScope()

	var isGamePlaying by remember {
		mutableStateOf(true)
	}
	var sizeLength by remember {
		mutableStateOf(0f)
	}
	var cellLength by remember {
		mutableStateOf(0f)
	}
	var translateOffset by remember {
		mutableStateOf(Offset.Zero)
	}

	var playGroundRectArray by remember {
		mutableStateOf(
			Array(3) {
				Array(3) {
					Rect(
						offset = Offset.Zero,
						size = Size.Zero
					)
				}
			}
		)
	}
	var playGroundSelectStateArray by remember {
		mutableStateOf(emptyGameStates())
	}

	var currentTouchRowColumn: RowColumn? by remember {
		mutableStateOf(null)
	}

	var currentPlayer by remember {
		mutableStateOf<Player>(Player.CirclePlayer)
	}

	var animationArray = remember {
		emptyAnimations()
	}

	var turnCount by remember {
		mutableStateOf(0)
	}

	Canvas(
		modifier = modifier
			.pointerInput(true) {
				detectTapGestures {
					if (!isGamePlaying) {
						return@detectTapGestures
					}

					currentTouchRowColumn = playGroundRectArray.findTouchRowColumn(it)
					currentTouchRowColumn?.let { rowColumn ->
						if (playGroundSelectStateArray[rowColumn.row][rowColumn.column] != null) {
							return@let
						}

						val newPlayGroundSelectStateArray = playGroundSelectStateArray.copyOf()
						newPlayGroundSelectStateArray[rowColumn.row][rowColumn.column] = currentPlayer
						playGroundSelectStateArray = newPlayGroundSelectStateArray

						scope.setAnimationValueToOne(animationArray[rowColumn.row][rowColumn.column])

						val winner = playGroundSelectStateArray.checkWinner()?.apply {
							onPlayerWin(this)
						}

						currentPlayer = currentPlayer.nextPlayer()
						turnCount++

						if (winner == null && turnCount == 9) {
							onPlayerWin(null)
						}

						if (turnCount == 9 || winner != null) {
							scope.launch {
								isGamePlaying = false
								delay(5000)
								animationArray = emptyAnimations()
								playGroundSelectStateArray = emptyGameStates()
								onNewGame()
								isGamePlaying = true
								turnCount = 0
							}
						}
					}
				}
			}
	) {
		sizeLength = size.minDimension
		cellLength = sizeLength / 3

		playGroundRectArray = Array(3) { row ->
			Array(3) { col ->
				Rect(
					offset = Offset(
						x = translateOffset.x + col * cellLength,
						y = translateOffset.y + row * cellLength,
					),
					size = Size(cellLength, cellLength)
				)
			}
		}

		val playGroundPath = Path().apply {
			moveTo(0f, cellLength)
			lineTo(sizeLength, cellLength)
			moveTo(0f, cellLength * 2)
			lineTo(sizeLength, cellLength * 2)
			moveTo(cellLength, 0f)
			lineTo(cellLength, sizeLength)
			moveTo(cellLength * 2, 0f)
			lineTo(cellLength * 2, sizeLength)
		}

		translateOffset = Offset(
			x = if (size.maxDimension == size.width) {
				center.x - sizeLength / 2
			} else {
				0f
			},
			y = if (size.maxDimension == size.height) {
				center.y - sizeLength / 2
			} else {
				0f
			}
		)

		translate(
			left = translateOffset.x,
			top = translateOffset.y
		) {
			drawPath(
				path = playGroundPath,
				color = Color.Black,
				style = Stroke(
					width = 10f,
					cap = StrokeCap.Round
				)
			)

			playGroundSelectStateArray.forEachIndexed { row, arrayOfPlayers ->
				arrayOfPlayers.forEachIndexed { column, player ->
					player?.let {
						when (it) {
							is Player.CirclePlayer -> {
								val circlePath = Path().apply {
									addArc(
										oval = Rect(
											center = Offset(
												x = column * cellLength + cellLength / 2,
												y = row * cellLength + cellLength / 2
											),
											radius = cellLength / 4
										),
										startAngleDegrees = 0f,
										sweepAngleDegrees = animationArray[row][column].value * 360f
									)
								}
								drawPath(
									path = circlePath,
									color = Color.Green,
									style = Stroke(
										width = 10f,
										cap = StrokeCap.Round
									)
								)
							}
							is Player.CrossXPlayer -> {
								val crossXPath1 = Path().apply {
									moveTo(
										x = column * cellLength + cellLength / 4,
										y = row * cellLength + cellLength / 4
									)
									lineTo(
										x = column * cellLength + cellLength - cellLength / 4,
										y = row * cellLength + cellLength - cellLength / 4
									)
								}
								val crossXPath2 = Path().apply {
									moveTo(
										x = column * cellLength + cellLength / 4,
										y = row * cellLength + cellLength - cellLength / 4
									)
									lineTo(
										x = column * cellLength + cellLength - cellLength / 4,
										y = row * cellLength + cellLength / 4
									)
								}

								val outPath1 = Path()

								PathMeasure().apply {
									setPath(crossXPath1, false)
									getSegment(0f, animationArray[row][column].value * length, outPath1, true)
								}

								val outPath2 = Path()

								PathMeasure().apply {
									setPath(crossXPath2, false)
									getSegment(0f, animationArray[row][column].value * length, outPath2, true)
								}

								drawPath(
									path = outPath1,
									color = Color.Red,
									style = Stroke(
										width = 10f,
										cap = StrokeCap.Round
									)
								)
								drawPath(
									path = outPath2,
									color = Color.Red,
									style = Stroke(
										width = 10f,
										cap = StrokeCap.Round
									)
								)
							}
						}
					}
				}
			}
		}

	}
}

private fun emptyGameStates() = Array(3) {
	Array<Player?>(3) {
		null
	}
}

private fun emptyAnimations(): ArrayList<ArrayList<Animatable<Float, AnimationVector1D>>> {
	val arrayList = arrayListOf<ArrayList<Animatable<Float, AnimationVector1D>>>()
	for (row in 0..2) {
		arrayList.add(arrayListOf())
		for (col in 0..2) {
			arrayList[row].add(Animatable(0f))
		}
	}

	return arrayList
}

fun CoroutineScope.setAnimationValueToOne(animatable: Animatable<Float, AnimationVector1D>) {
	launch {
		animatable.animateTo(
			targetValue = 1f,
			animationSpec = tween(1000)
		)
	}
}

fun Array<Array<Rect>>.findTouchRowColumn(touchOffset: Offset): RowColumn? {
	forEachIndexed { row, arrayOfRects ->
		arrayOfRects.forEachIndexed { column, rect ->
			if (rect.contains(touchOffset)) {
				return RowColumn(row, column)
			}
		}
	}

	return null
}

fun Array<Array<Player?>>.checkWinner(): Player? {
	forEach {
		val isCirclePlayerWin = it.all { player ->
			player == Player.CirclePlayer
		}

		if (isCirclePlayerWin) {
			return Player.CirclePlayer
		}

		val isCrossXPlayerWin = it.all { player ->
			player == Player.CrossXPlayer
		}

		if (isCrossXPlayerWin) {
			return Player.CrossXPlayer
		}
	}

	for (column in 0..2) {
		var isCirclePlayerWin = true
		for (row in 0..2) {
			if (this[row][column] != Player.CirclePlayer) {
				isCirclePlayerWin = false
				break
			}
		}
		if (isCirclePlayerWin) {
			return Player.CirclePlayer
		}

		var isCrossXPlayerWin = true
		for (row in 0..2) {
			if (this[row][column] != Player.CrossXPlayer) {
				isCrossXPlayerWin = false
				break
			}
		}

		if (isCrossXPlayerWin) {
			return Player.CrossXPlayer
		}
	}

	if (this[0][0] == this[1][1] && this[1][1] == this[2][2] && this[0][0] == Player.CirclePlayer) {
		return Player.CirclePlayer
	}

	if (this[0][2] == this[1][1] && this[1][1] == this[2][0] && this[0][2] == Player.CirclePlayer) {
		return Player.CirclePlayer
	}

	if (this[0][0] == this[1][1] && this[1][1] == this[2][2] && this[0][0] == Player.CrossXPlayer) {
		return Player.CrossXPlayer
	}

	if (this[0][2] == this[1][1] && this[1][1] == this[2][0] && this[0][2] == Player.CrossXPlayer) {
		return Player.CrossXPlayer
	}

	return null
}

data class RowColumn(
	val row: Int,
	val column: Int
)

sealed class Player {
	object CirclePlayer: Player()
	object CrossXPlayer: Player()

	fun nextPlayer(): Player {
		return when (this) {
			is CirclePlayer -> CrossXPlayer
			is CrossXPlayer -> CirclePlayer
		}
	}
}