/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.route

import android.os.Parcel
import android.os.Parcelable
import kotlinx.datetime.LocalTime
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data object Home : Parcelable

sealed class SubRoutes {
    @Serializable
    @Parcelize
    data object ChipTextField : SubRoutes(), Parcelable

    @Serializable
    @Parcelize
    data class TimePicker(
        val initialTime: LocalTime,
    ) : SubRoutes(),
        Parcelable {
        private companion object : Parceler<TimePicker> {
            override fun TimePicker.write(
                parcel: Parcel,
                flags: Int,
            ) {
                parcel.writeInt(initialTime.toMillisecondOfDay())
            }

            override fun create(parcel: Parcel): TimePicker =
                TimePicker(
                    LocalTime.fromMillisecondOfDay(parcel.readInt()),
                )
        }
    }

    @Parcelize
    @Serializable
    data object SharedElementTransition : SubRoutes(), Parcelable

    @Parcelize
    @Serializable
    data object Reveal : SubRoutes(), Parcelable

    @Parcelize
    @Serializable
    data object Settings : SubRoutes(), Parcelable

    @Parcelize
    @Serializable
    data object ParallaxScrolling : SubRoutes(), Parcelable

    @Parcelize
    @Serializable
    data object Haze : SubRoutes(), Parcelable
}

sealed class SharedElementRoutes {
    @Serializable
    @Parcelize
    data object Main : SharedElementRoutes(), Parcelable

    @Parcelize
    @Serializable
    data class Details(
        val id: Int,
    ) : SharedElementRoutes(),
        Parcelable
}
