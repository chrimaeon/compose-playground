/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.cmgapps.android.compose.route.Home
import com.cmgapps.android.compose.route.SubRoutes
import com.cmgapps.android.compose.screen.molecule.MoleculeScreen
import com.cmgapps.android.compose.toLocalTime
import com.cmgapps.android.compose.viewmodel.NavigationViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ListDetailSceneStrategy<*>.isExpanded() = directive.maxHorizontalPartitions > 1

@OptIn(ExperimentalTime::class)
private val routeItems =
    listOf(
        SubRoutes.ChipTextField,
        SubRoutes.TimePicker(Clock.System.now().toLocalTime()),
        SubRoutes.SharedElementTransition,
        SubRoutes.Reveal,
        SubRoutes.ParallaxScrolling,
        SubRoutes.Haze,
        SubRoutes.PullToRefresh,
        SubRoutes.Molecule,
        SubRoutes.AnimateItem,
        SubRoutes.TextFieldTransformation,
    )

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    navigationViewModel: NavigationViewModel = viewModel(),
    deepLink: Uri? = null,
    dogCeoServerBaseUrl: String = "https://dog.ceo/api/",
) {
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

    LaunchedEffect(deepLink) {
        deepLink?.pathSegments?.let {
            if (it.isEmpty()) {
                return@let
            }
            when (it.firstOrNull()) {
                "cupcake" -> navigationViewModel.push(SubRoutes.SharedElementTransition)
            }
        }
    }

    NavDisplay(
        modifier = modifier,
        backStack = navigationViewModel.backstack,
        onBack = { keysToRemove -> repeat(keysToRemove) { navigationViewModel.pop() } },
        sceneStrategy = listDetailStrategy,
        entryProvider =
            entryProvider {
                entry<Home>(
                    metadata = ListDetailSceneStrategy.listPane { Box(Modifier.fillMaxSize()) },
                ) {
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    navigationViewModel.push(SubRoutes.Settings)
                                },
                            ) {
                                Icon(
                                    Icons.Outlined.Settings,
                                    contentDescription = stringResource(SubRoutes.Settings.titleResource),
                                )
                            }
                        },
                    ) { contentPadding ->
                        LazyColumn(
                            modifier = Modifier.padding(contentPadding),
                            contentPadding =
                                PaddingValues(
                                    bottom = 64.dp,
                                ),
                        ) {
                            items(routeItems) { item ->
                                NavigationItem(
                                    title = stringResource(id = item.titleResource),
                                    onClick = {
                                        navigationViewModel.push(item)
                                    },
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
                entry<SubRoutes.ChipTextField>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    ChipTextFieldScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }
                entry<SubRoutes.TimePicker>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    TimePickerScreen(
                        initialTime = it.initialTime,
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }
                entry<SubRoutes.SharedElementTransition>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    SharedElementTransitionScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }

                entry<SubRoutes.Reveal>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    RevealScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }

                entry<SubRoutes.Settings>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    SettingsScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }

                entry<SubRoutes.ParallaxScrolling>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    ParallaxScrollingScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }

                entry<SubRoutes.Haze>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    HazeScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }

                entry<SubRoutes.PullToRefresh>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    PullToRefreshScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }

                entry<SubRoutes.Molecule>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    MoleculeScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                        serverBaseUrl = dogCeoServerBaseUrl,
                    )
                }

                entry<SubRoutes.AnimateItem>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    AnimateItemScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }

                entry<SubRoutes.TextFieldTransformation>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) {
                    TextFieldTransformationScreen(
                        backButton = {
                            BackButton(
                                isVisible = !listDetailStrategy.isExpanded(),
                                onClick = navigationViewModel::pop,
                            )
                        },
                    )
                }
            },
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun BackButton(
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandHorizontally(),
        exit = shrinkHorizontally() + fadeOut(),
    ) {
        IconButton(
            onClick = onClick,
            content = {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            },
        )
    }
}

@Composable
private fun NavigationItem(
    title: String,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(title) },
        trailingContent = {
            IconButton(onClick = onClick) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = "Go to $title",
                )
            }
        },
    )
}
