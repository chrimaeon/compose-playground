/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.route

import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.cmgapps.android.compose.R
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data object Home : NavKey

@Serializable
sealed class SubRoutes(
    @param:StringRes val titleResource: Int,
) : NavKey {
    @Serializable
    data object ChipTextField : SubRoutes(R.string.chip_text_field)

    @Serializable
    data class TimePicker(
        val initialTime: LocalTime,
    ) : SubRoutes(R.string.time_picker)

    @Serializable
    data object SharedElementTransition : SubRoutes(R.string.shared_element_transition)

    @Serializable
    data object Reveal : SubRoutes(R.string.reveal)

    @Serializable
    data object Settings : SubRoutes(R.string.settings)

    @Serializable
    data object ParallaxScrolling : SubRoutes(R.string.parallax_scrolling)

    @Serializable
    data object Haze : SubRoutes(R.string.haze)

    @Serializable
    data object PullToRefresh : SubRoutes(R.string.pull_2_refresh)

    @Serializable
    data object Molecule : SubRoutes(R.string.molecule)

    @Serializable
    data object AnimateItem : SubRoutes(R.string.animate_item)

    @Serializable
    data object TextFieldTransformation : SubRoutes(R.string.textfield_transformation)
}

sealed class SharedElementRoutes : NavKey {
    @Serializable
    data object Main : SharedElementRoutes()

    @Serializable
    data class Details(
        val id: Int,
    ) : SharedElementRoutes()
}
