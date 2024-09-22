/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.screen.molecule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cmgapps.android.compose.service.PupperPicsService
import kotlinx.coroutines.flow.Flow

sealed interface Event {
    data class SelectBreed(
        val breed: Breed,
    ) : Event

    data object FetchAgain : Event
}

data class Model(
    val loading: Boolean,
    val breeds: List<Breed>,
    val dropdownText: String?,
    val currentUrl: String?,
)

@Composable
fun pupperPicsPresenter(
    events: Flow<Event>,
    service: PupperPicsService,
): Model {
    var breeds: List<Breed> by remember { mutableStateOf(emptyList()) }
    var currentBreed: Breed? by remember { mutableStateOf(null) }
    var currentUrl: String? by remember { mutableStateOf(null) }
    var fetchId: Int by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        breeds = service.listBreeds()
        currentBreed = breeds.first()
    }

    LaunchedEffect(currentBreed, fetchId) {
        currentUrl = null
        currentUrl = currentBreed?.let { service.randomImageUrlFor(it.urlPath) }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Event.SelectBreed -> currentBreed = event.breed
                Event.FetchAgain -> fetchId++ // Incrementing fetchId will load another random image URL.
            }
        }
    }

    return Model(
        loading = currentBreed == null,
        breeds = breeds,
        dropdownText = currentBreed?.name,
        currentUrl = currentUrl,
    )
}

data class Breed(
    val name: String,
    val urlPath: String,
)
