/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.service

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import com.cmgapps.android.compose.screen.molecule.Breed
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable

interface PupperPicsService {
    suspend fun listBreeds(): List<Breed>

    suspend fun randomImageUrlFor(breed: String): String
}

fun PupperPicsService(): PupperPicsService {
    val api =
        HttpClient(OkHttp) {
            engine {
            }
            defaultRequest {
                url("https://dog.ceo/api/")
            }

            install(ContentNegotiation) {
                json()
            }
        }

    return object : PupperPicsService {
        override suspend fun listBreeds(): List<Breed> =
            api
                .get {
                    url {
                        path("breeds/list/all")
                    }
                }.body<ListResponse>()
                .message
                .flatMap { (breed, subBreeds) ->
                    if (subBreeds.isEmpty()) {
                        listOf(Breed(breed.capitalize(Locale.current), breed))
                    } else {
                        subBreeds.map { subBreed ->
                            Breed(
                                "${breed.capitalize(Locale.current)} ${subBreed.capitalize(Locale.current)}",
                                "$breed/$subBreed",
                            )
                        }
                    }
                }

        override suspend fun randomImageUrlFor(breed: String): String =
            api
                .get {
                    url {
                        path("breed/$breed/images/random")
                    }
                }.body<ImageResponse>()
                .message
    }
}

@Serializable
private data class ListResponse(
    val message: Map<String, List<String>>,
    val status: String,
)

@Serializable
private data class ImageResponse(
    val message: String,
    val status: String,
)
