/*
 * Copyright (c) 2025. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import com.cmgapps.android.compose.route.Home
import com.cmgapps.android.compose.route.SubRoutes

class NavigationViewModel : ViewModel() {
    private val _backstack: SnapshotStateList<NavKey> = mutableStateListOf(Home)
    val backstack: List<NavKey>
        get() = _backstack

    fun push(subRoute: SubRoutes) {
        _backstack += subRoute
    }

    fun pop() {
        _backstack.removeLastOrNull()
    }
}
