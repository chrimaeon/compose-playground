/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cmgapps.android.compose.R
import com.cmgapps.android.compose.route.SubRoutes
import com.cmgapps.android.compose.toLocalTime
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun ThreePaneScaffoldNavigator<*>.isListExpanded() =
    scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
fun ThreePaneScaffoldNavigator<*>.isDetailExpanded() =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    scaffoldNavigator: ThreePaneScaffoldNavigator<SubRoutes> = rememberListDetailPaneScaffoldNavigator<SubRoutes>(),
) {
    val backBehavior =
        if (scaffoldNavigator.isListExpanded() &&
            scaffoldNavigator.isDetailExpanded()
        ) {
            BackNavigationBehavior.PopUntilContentChange
        } else {
            BackNavigationBehavior.PopUntilScaffoldValueChange
        }
    BackHandler(
        enabled =
            scaffoldNavigator.canNavigateBack(backBehavior),
    ) {
        scaffoldNavigator.navigateBack(
            backBehavior,
        )
    }

    ListDetailPaneScaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        directive = scaffoldNavigator.scaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        listPane = {
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                SubRoutes.Settings,
                            )
                        },
                    ) {
                        Icon(Icons.Outlined.Settings, "App Info")
                    }
                },
            ) { contentPadding ->
                AnimatedPane(
                    modifier =
                        Modifier
                            .padding(contentPadding)
                            .fillMaxSize(),
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 64.dp),
                    ) {
                        item {
                            NavigationItem(
                                title = stringResource(id = R.string.chip_text_field),
                                onClick = {
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        SubRoutes.ChipTextField,
                                    )
                                },
                            )
                        }
                        item {
                            HorizontalDivider()
                        }
                        item {
                            NavigationItem(
                                title = stringResource(id = R.string.time_picker),
                                onClick = {
                                    val initialTime = Clock.System.now().toLocalTime()
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        SubRoutes.TimePicker(initialTime),
                                    )
                                },
                            )
                        }
                        item {
                            HorizontalDivider()
                        }
                        item {
                            NavigationItem(
                                title = stringResource(R.string.shared_element_transition),
                                onClick = {
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        SubRoutes.SharedElementTransition,
                                    )
                                },
                            )
                        }
                        item {
                            HorizontalDivider()
                        }
                        item {
                            NavigationItem(
                                title = stringResource(id = R.string.reveal),
                                onClick = {
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        SubRoutes.Reveal,
                                    )
                                },
                            )
                        }
                        item {
                            HorizontalDivider()
                        }
                        item {
                            NavigationItem(
                                title = stringResource(R.string.parallax_scrolling),
                                onClick = {
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        SubRoutes.ParallaxScrolling,
                                    )
                                },
                            )
                        }
                        item {
                            HorizontalDivider()
                        }
                        item {
                            NavigationItem(
                                title = stringResource(R.string.haze),
                                onClick = {
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        SubRoutes.Haze,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        },
        detailPane = {
            scaffoldNavigator.currentDestination?.content?.let {
                when (it) {
                    SubRoutes.ChipTextField ->
                        ChipTextFieldScreen(
                            backButton = {
                                BackButton(
                                    scaffoldNavigator = scaffoldNavigator,
                                    backBehavior = backBehavior,
                                )
                            },
                        )

                    is SubRoutes.TimePicker ->
                        TimePickerScreen(
                            initialTime = it.initialTime,
                            backButton = {
                                BackButton(
                                    scaffoldNavigator = scaffoldNavigator,
                                    backBehavior = backBehavior,
                                )
                            },
                        )

                    SubRoutes.SharedElementTransition -> SharedElementTransitionScreen()
                    SubRoutes.Reveal ->
                        RevealScreen(
                            backButton = {
                                BackButton(
                                    scaffoldNavigator = scaffoldNavigator,
                                    backBehavior = backBehavior,
                                )
                            },
                        )

                    SubRoutes.Settings ->
                        SettingsScreen(
                            backButton = {
                                BackButton(
                                    scaffoldNavigator = scaffoldNavigator,
                                    backBehavior = backBehavior,
                                )
                            },
                        )

                    SubRoutes.ParallaxScrolling ->
                        ParallaxScrollingScreen(
                            backButton = {
                                BackButton(
                                    scaffoldNavigator = scaffoldNavigator,
                                    backBehavior = backBehavior,
                                )
                            },
                        )

                    SubRoutes.Haze ->
                        HazeScreen(
                            backButton = {
                                BackButton(
                                    scaffoldNavigator = scaffoldNavigator,
                                    backBehavior = backBehavior,
                                )
                            },
                        )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun BackButton(
    scaffoldNavigator: ThreePaneScaffoldNavigator<*>,
    backBehavior: BackNavigationBehavior,
) {
    AnimatedVisibility(
        visible = !scaffoldNavigator.isListExpanded(),
    ) {
        IconButton(
            onClick = {
                scaffoldNavigator.navigateBack(backBehavior)
            },
            content = {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
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
        modifier =
            Modifier.clickable(onClick = onClick),
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
