/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.route

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.datetime.LocalTime
import kotlinx.datetime.serializers.LocalTimeIso8601Serializer
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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

val LocalTimeType =
    object : NavType<LocalTime>(
        isNullableAllowed = false,
    ) {
        override fun get(
            bundle: Bundle,
            key: String,
        ): LocalTime = LocalTime.fromSecondOfDay(bundle.getInt(key))

        override fun put(
            bundle: Bundle,
            key: String,
            value: LocalTime,
        ) {
            bundle.putInt(key, value.toSecondOfDay())
        }

        override fun parseValue(value: String): LocalTime = Json.decodeFromString(LocalTimeIso8601Serializer, value)

        override fun serializeAsValue(value: LocalTime): String = Json.encodeToString(LocalTimeIso8601Serializer, value)
    }
