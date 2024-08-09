/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cmgapps.android.compose.R
import com.svenjacobs.reveal.Reveal
import com.svenjacobs.reveal.RevealCanvas
import com.svenjacobs.reveal.RevealOverlayArrangement
import com.svenjacobs.reveal.RevealOverlayScope
import com.svenjacobs.reveal.RevealShape
import com.svenjacobs.reveal.RevealShape.Circle.asRect
import com.svenjacobs.reveal.effect.dim.DimRevealOverlayEffect
import com.svenjacobs.reveal.rememberRevealCanvasState
import com.svenjacobs.reveal.rememberRevealState
import com.svenjacobs.reveal.shapes.balloon.Arrow
import com.svenjacobs.reveal.shapes.balloon.Balloon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private enum class RevealKey(
    val step: Int,
) {
    Step1(0),
    Step2(1),
    Step3(2),
}

private fun RevealKey.next(): RevealKey? = RevealKey.entries.find { it.step == (this.step + 1) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevealScreen(
    backButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val revealCanvasState = rememberRevealCanvasState()

    RevealCanvas(
        modifier = modifier.fillMaxSize(),
        revealCanvasState = revealCanvasState,
    ) {
        val revealState = rememberRevealState()
        val scope = rememberCoroutineScope()

        LaunchedEffect(revealState) {
            if (!revealState.tryReveal(RevealKey.Step1)) {
                delay(500.milliseconds)
                revealState.tryReveal(RevealKey.Step1)
            }
        }

        Reveal(
            revealCanvasState = revealCanvasState,
            revealState = revealState,
            onOverlayClick = {
                scope.launch {
                    (it as RevealKey).next()?.let { next ->
                        revealState.reveal(next)
                    } ?: revealState.hide()
                }
            },
            overlayContent = { OverlayContent(it as RevealKey) },
            overlayEffect =
                DimRevealOverlayEffect(
                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.7f),
                ),
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text(stringResource(R.string.reveal)) },
                        navigationIcon = backButton,
                        actions = {
                            IconButton(onClick = { scope.launch { revealState.reveal(RevealKey.Step1) } }) {
                                Icon(
                                    Icons.Default.Replay,
                                    contentDescription =
                                        stringResource(
                                            R.string.restart_onboarding,
                                        ),
                                )
                            }
                        },
                    )
                },
            ) { contentPadding ->
                Column(
                    modifier =
                        Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier =
                            Modifier
                                .revealable(
                                    key = RevealKey.Step1,
                                    onClick = { scope.launch { revealState.reveal(RevealKey.Step2) } },
                                    shape = MaterialTheme.shapes.medium.toRevealShape(),
                                ).clickable { scope.launch { revealState.reveal(RevealKey.Step1) } },
                        text = "Step One",
                    )
                    Spacer(modifier = Modifier.height(100.dp))

                    Text(
                        modifier =
                            Modifier
                                .revealable(
                                    key = RevealKey.Step2,
                                    onClick = { scope.launch { revealState.reveal(RevealKey.Step3) } },
                                    shape = MaterialTheme.shapes.medium.toRevealShape(),
                                ).clickable { scope.launch { revealState.reveal(RevealKey.Step2) } },
                        text = "Step Two",
                    )

                    Spacer(modifier = Modifier.height(100.dp))

                    Text(
                        modifier =
                            Modifier
                                .revealable(
                                    key = RevealKey.Step3,
                                    onClick = { scope.launch { revealState.hide() } },
                                    shape = MaterialTheme.shapes.medium.toRevealShape(),
                                ).clickable { scope.launch { revealState.reveal(RevealKey.Step3) } },
                        text = "Step Three",
                    )
                }
            }
        }
    }
}

@Composable
private fun RevealOverlayScope.OverlayContent(key: RevealKey) {
    when (key) {
        RevealKey.Step1 ->
            OverlayText(
                stringResource(R.string.onboarding_step1),
                modifier =
                    Modifier.align(
                        verticalArrangement = RevealOverlayArrangement.Top,
                    ),
                arrow = Arrow.bottom(),
            )

        RevealKey.Step2 ->
            OverlayText(
                stringResource(R.string.onboarding_step2),
                modifier =
                    Modifier.align(
                        horizontalArrangement = RevealOverlayArrangement.Start,
                    ),
                arrow = Arrow.end(),
            )

        RevealKey.Step3 ->
            OverlayText(
                stringResource(R.string.onboarding_step3),
                modifier =
                    Modifier.align(
                        verticalArrangement = RevealOverlayArrangement.Bottom,
                    ),
                arrow = Arrow.top(),
            )
    }
}

@Composable
private fun OverlayText(
    text: String,
    arrow: Arrow,
    modifier: Modifier = Modifier,
) {
    Balloon(
        modifier = modifier.padding(8.dp),
        arrow = arrow,
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        elevation = 2.dp,
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
            textAlign = TextAlign.Center,
        )
    }
}

fun CornerBasedShape.toRevealShape() =
    RevealShape.Custom(
        onClip = { size, density, layoutDirection ->
            val (topLeft, topRight, bottomRight, bottomLeft) =
                when (layoutDirection) {
                    LayoutDirection.Ltr ->
                        listOf(
                            this@toRevealShape.topStart,
                            this@toRevealShape.topEnd,
                            this@toRevealShape.bottomEnd,
                            this@toRevealShape.bottomStart,
                        )

                    LayoutDirection.Rtl ->
                        listOf(
                            this@toRevealShape.topEnd,
                            this@toRevealShape.topStart,
                            this@toRevealShape.bottomStart,
                            this@toRevealShape.bottomEnd,
                        )
                }

            Path().apply {
                addRoundRect(
                    RoundRect(
                        size.asRect(),
                        topLeft = topLeft.toCornerRadius(size, density),
                        topRight = topRight.toCornerRadius(size, density),
                        bottomRight = bottomRight.toCornerRadius(size, density),
                        bottomLeft = bottomLeft.toCornerRadius(size, density),
                    ),
                )
            }
        },
    )

fun CornerSize.toCornerRadius(
    shapeSize: Size,
    density: Density,
) = CornerRadius(toPx(shapeSize, density))
