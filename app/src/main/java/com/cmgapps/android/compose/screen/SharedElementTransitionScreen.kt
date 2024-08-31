/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmgapps.android.compose.R
import com.cmgapps.android.compose.route.SharedElementRoutes

private const val IMAGE_KEY = "image-key"
private const val TEXT_KEY = "label"
private const val PLACEHOLDER_SMALL_KEY = "placeholder-small"
private const val PLACEHOLDER_LARGE_KEY = "placeholder-large"

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementTransitionScreen(modifier: Modifier = Modifier) {
    SharedTransitionLayout(
        modifier = modifier,
    ) {
        val navController = rememberNavController()

        val cupcakes =
            remember { mutableStateListOf<Cupcake>() }

        LaunchedEffect(Unit) {
            cupcakes.addAll(List(8) { Cupcake(it) }.shuffled())
        }

        val imageBoundsTransform =
            BoundsTransform { _, _ ->
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow,
                )
            }

        NavHost(navController = navController, startDestination = SharedElementRoutes.Main) {
            composable<SharedElementRoutes.Main> {
                MainContent(
                    cupcakes = cupcakes,
                    onShowDetails = { id -> navController.navigate(SharedElementRoutes.Details(id)) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    imageBoundsTransform = imageBoundsTransform,
                )
            }
            composable<SharedElementRoutes.Details> { backstack ->
                val details = backstack.toRoute<SharedElementRoutes.Details>()
                DetailsContent(
                    cupcake = cupcakes.first { it.id == details.id },
                    onBack = navController::popBackStack,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    imageBoundsTransform = imageBoundsTransform,
                )
            }
        }
    }
}

@VisibleForTesting
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainContent(
    cupcakes: List<Cupcake>,
    onShowDetails: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    imageBoundsTransform: BoundsTransform,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentPadding =
                with(LocalLayoutDirection.current) {
                    PaddingValues(
                        start = contentPadding.calculateStartPadding(this),
                        top = contentPadding.calculateTopPadding() + 8.dp,
                        end = contentPadding.calculateEndPadding(this),
                        bottom = contentPadding.calculateBottomPadding() + 8.dp,
                    )
                },
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                cupcakes,
                key = { cupcake -> cupcake.id },
            ) { cupcake ->

                Card(
                    onClick = { onShowDetails(cupcake.id) },
                ) {
                    var backgroundGradient: Brush by
                        remember {
                            mutableStateOf(
                                Brush.verticalGradient(
                                    listOf(Color.White, Color.LightGray),
                                ),
                            )
                        }
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(backgroundGradient)
                                .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        with(sharedTransitionScope) {
                            AsyncImage(
                                model =
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(cupcake.previewUrl)
                                        .crossfade(true)
                                        .placeholder(R.drawable.cupcake_placeholder)
                                        .placeholderMemoryCacheKey(PLACEHOLDER_SMALL_KEY)
                                        .memoryCacheKey(IMAGE_KEY + cupcake.id)
                                        .build(),
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .sharedBounds(
                                            rememberSharedContentState(
                                                key = IMAGE_KEY + cupcake.id,
                                            ),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            boundsTransform = imageBoundsTransform,
                                        ).size(120.dp)
                                        .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                onSuccess = { result ->
                                    Palette
                                        .from(
                                            result.result.drawable
                                                .toBitmap()
                                                .copy(Bitmap.Config.ARGB_8888, false),
                                        ).generate {
                                            it?.let { palette ->
                                                backgroundGradient =
                                                    Brush.verticalGradient(
                                                        listOf(
                                                            Color(
                                                                palette.getLightMutedColor(
                                                                    android.graphics.Color.WHITE,
                                                                ),
                                                            ),
                                                            Color(
                                                                palette.getMutedColor(
                                                                    android.graphics.Color.LTGRAY,
                                                                ),
                                                            ),
                                                        ),
                                                    )
                                            }
                                        }
                                },
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                cupcake.title,
                                modifier =
                                    Modifier.sharedBounds(
                                        rememberSharedContentState(key = TEXT_KEY + cupcake.id),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    ),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@VisibleForTesting
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsContent(
    cupcake: Cupcake,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    imageBoundsTransform: BoundsTransform,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    with(sharedTransitionScope) {
                        Text(
                            cupcake.title,
                            modifier =
                                Modifier.sharedBounds(
                                    rememberSharedContentState(key = TEXT_KEY + cupcake.id),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                ),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { contentPadding ->

        Box(
            modifier =
                Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            with(sharedTransitionScope) {
                AsyncImage(
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(cupcake.largeUrl)
                            .crossfade(true)
                            .placeholder(R.drawable.cupcake_placeholder) // same key as shared element key
                            .placeholderMemoryCacheKey(PLACEHOLDER_LARGE_KEY) //  same key as shared element key
                            .memoryCacheKey("large_image" + cupcake.id)
                            .build(),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .sharedBounds(
                                rememberSharedContentState(
                                    key = IMAGE_KEY + cupcake.id,
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = imageBoundsTransform,
                            ).size(300.dp)
                            .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

data class Cupcake(
    val id: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readInt())

    val previewUrl =
        "file:///android_asset/cupcake${id}_preview.jpg"

    val largeUrl =
        "file:///android_asset/cupcake$id.jpg"

    val title = "Cupcake #$id"

    override fun writeToParcel(
        parcel: Parcel,
        flags: Int,
    ) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Cupcake> {
        override fun createFromParcel(parcel: Parcel): Cupcake = Cupcake(parcel)

        override fun newArray(size: Int): Array<Cupcake?> = arrayOfNulls(size)
    }
}
