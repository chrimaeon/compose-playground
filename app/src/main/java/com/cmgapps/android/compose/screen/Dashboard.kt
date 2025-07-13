/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cmgapps.android.compose.R
import com.cmgapps.android.compose.route.SubRoutes
import com.cmgapps.android.compose.screen.molecule.MoleculeScreen
import com.cmgapps.android.compose.toLocalTime
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun ThreePaneScaffoldNavigator<*>.isExpanded(role: ThreePaneScaffoldRole) =
    scaffoldValue[role] == PaneAdaptedValue.Expanded

private data class DetailRouteItem(
    @param:StringRes
    val titleResource: Int,
    val contentKey: SubRoutes,
)

private val routeItems =
    listOf(
        DetailRouteItem(R.string.chip_text_field, SubRoutes.ChipTextField),
        DetailRouteItem(
            R.string.time_picker,
            @OptIn(ExperimentalTime::class)
            SubRoutes.TimePicker(Clock.System.now().toLocalTime()),
        ),
        DetailRouteItem(R.string.shared_element_transition, SubRoutes.SharedElementTransition),
        DetailRouteItem(R.string.reveal, SubRoutes.Reveal),
        DetailRouteItem(R.string.parallax_scrolling, SubRoutes.ParallaxScrolling),
        DetailRouteItem(R.string.haze, SubRoutes.Haze),
        DetailRouteItem(R.string.pull_2_refresh, SubRoutes.PullToRefresh),
        DetailRouteItem(R.string.molecule, SubRoutes.Molecule),
        DetailRouteItem(R.string.animate_item, SubRoutes.AnimateItem),
        DetailRouteItem(R.string.textfield_transformation, SubRoutes.TextFieldTransformation),
    )

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Dashboard(
    modifier: Modifier = Modifier,
    scaffoldNavigator: ThreePaneScaffoldNavigator<SubRoutes> = rememberListDetailPaneScaffoldNavigator<SubRoutes>(),
    deepLink: Uri? = null,
    dogCeoServerBaseUrl: String = "https://dog.ceo/api/",
) {
    val coroutineScope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        SubRoutes.Settings,
                                    )
                                }
                            },
                        ) {
                            Icon(Icons.Outlined.Settings, "App Info")
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
                                    coroutineScope.launch {
                                        scaffoldNavigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            item.contentKey,
                                        )
                                    }
                                },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                scaffoldNavigator.currentDestination?.contentKey?.let {
                    when (it) {
                        SubRoutes.ChipTextField ->
                            ChipTextFieldScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        is SubRoutes.TimePicker ->
                            TimePickerScreen(
                                initialTime = it.initialTime,
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        SubRoutes.SharedElementTransition ->
                            SharedElementTransitionScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        SubRoutes.Reveal ->
                            RevealScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        SubRoutes.Settings ->
                            SettingsScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        SubRoutes.ParallaxScrolling ->
                            ParallaxScrollingScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        SubRoutes.Haze ->
                            HazeScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        SubRoutes.PullToRefresh ->
                            PullToRefreshScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        SubRoutes.Molecule ->
                            MoleculeScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                                serverBaseUrl = dogCeoServerBaseUrl,
                            )

                        SubRoutes.AnimateItem ->
                            AnimateItemScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )

                        SubRoutes.TextFieldTransformation ->
                            TextFieldTransformationScreen(
                                backButton = {
                                    BackButton(
                                        isVisible = !scaffoldNavigator.isExpanded(ListDetailPaneScaffoldRole.List),
                                        onClick = {
                                            coroutineScope.launch {
                                                scaffoldNavigator.navigateBack()
                                            }
                                        },
                                    )
                                },
                            )
                    }
                }
            }

            LaunchedEffect(deepLink) {
                deepLink?.pathSegments?.let {
                    if (it.isEmpty()) {
                        return@let
                    }
                    when (it.firstOrNull()) {
                        "cupcake" ->
                            scaffoldNavigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                SubRoutes.SharedElementTransition,
                            )
                    }
                }
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
