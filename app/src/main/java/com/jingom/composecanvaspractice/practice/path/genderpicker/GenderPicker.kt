package com.jingom.composecanvaspractice.practice.path.genderpicker

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jingom.composecanvaspractice.R

@Composable
fun GenderPicker(
	modifier: Modifier = Modifier,
	gapBetweenGender: Dp = 50.dp,
	scaleFactor: Float = 7f,
	maleGradient: List<Color> = listOf(Color(0xFF6084CA), Color.Blue),
	femaleGradient: List<Color> = listOf(Color(0xFFC277D5), Color.Magenta),
	onGenderSelected: (Gender) -> Unit
) {
	val malePathString = stringResource(id = R.string.male_path)
	val femalePathString = stringResource(id = R.string.female_path)

	val malePath = remember {
		PathParser().parsePathString(malePathString).toPath()
	}
	val femalePath = remember {
		PathParser().parsePathString(femalePathString).toPath()
	}

	val malePathBound = remember {
		malePath.getBounds()
	}
	val femalePathBound = remember {
		femalePath.getBounds()
	}

	var currentlySelectedGender by remember {
		mutableStateOf<Gender>(Gender.Female)
	}

	var maleTranslationOffset by remember {
		mutableStateOf(Offset.Zero)
	}
	var femaleTranslationOffset by remember {
		mutableStateOf(Offset.Zero)
	}

	var currentlyClickOffset by remember {
		mutableStateOf(Offset.Zero)
	}

	val maleSelectionRadius = animateFloatAsState(
		targetValue = if (currentlySelectedGender is Gender.Male) 80f else 0f,
		animationSpec = tween(500)
	)

	val femaleSelectionRadius = animateFloatAsState(
		targetValue = if (currentlySelectedGender is Gender.Female) 80f else 0f,
		animationSpec = tween(500)
	)

	Canvas(
		modifier = modifier
			.pointerInput(true) {
				detectTapGestures {
					val transformedMaleRect = Rect(
						offset = maleTranslationOffset,
						size = malePathBound.size * scaleFactor
					)

					val transformedFemaleRect = Rect(
						offset = femaleTranslationOffset,
						size = femalePathBound.size * scaleFactor
					)

					if (currentlySelectedGender !is Gender.Male && transformedMaleRect.contains(it)) {
						currentlyClickOffset = it
						currentlySelectedGender = Gender.Male
						onGenderSelected(Gender.Male)
					} else if (currentlySelectedGender !is Gender.Female && transformedFemaleRect.contains(it)) {
						currentlyClickOffset = it
						currentlySelectedGender = Gender.Female
						onGenderSelected(Gender.Female)
					}
				}
			}
	) {
		maleTranslationOffset = Offset(
			x = center.x - malePathBound.width * scaleFactor - gapBetweenGender.toPx() / 2,
			y = center.y - malePathBound.height * scaleFactor / 2
		)

		femaleTranslationOffset = Offset(
			x = center.x + gapBetweenGender.toPx() / 2,
			y = center.y - femalePathBound.height * scaleFactor / 2
		)

		val untransformedMaleClickOffset = if (currentlyClickOffset == Offset.Zero) {
			malePathBound.center
		} else {
			(currentlyClickOffset - maleTranslationOffset) / scaleFactor
		}

		val untransformedFemaleClickOffset = if (currentlyClickOffset == Offset.Zero) {
			femalePathBound.center
		} else {
			(currentlyClickOffset - femaleTranslationOffset) / scaleFactor
		}

		translate(
			left = maleTranslationOffset.x,
			top = maleTranslationOffset.y
		) {
			scale(
				scale = scaleFactor,
				pivot = Offset.Zero
			) {
				drawPath(
					path = malePath,
					color = Color.LightGray
				)
				clipPath(malePath) {
					drawCircle(
						brush = Brush.radialGradient(
							colors = maleGradient,
							center = untransformedMaleClickOffset,
							radius = maleSelectionRadius.value + 1f
						),
						radius = maleSelectionRadius.value,
						center = untransformedMaleClickOffset
					)
				}
			}
		}

		translate(
			left = femaleTranslationOffset.x,
			top = femaleTranslationOffset.y
		) {
			scale(
				scale = scaleFactor,
				pivot = Offset.Zero
			) {
				drawPath(
					path = femalePath,
					color = Color.LightGray
				)
				clipPath(femalePath) {
					drawCircle(
						brush = Brush.radialGradient(
							colors = femaleGradient,
							center = untransformedFemaleClickOffset,
							radius = femaleSelectionRadius.value + 1f
						),
						radius = femaleSelectionRadius.value,
						center = untransformedFemaleClickOffset
					)
				}
			}
		}
	}
}