/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cmgapps.android.compose.screen.molecule.Event
import com.cmgapps.android.compose.screen.molecule.Model
import com.cmgapps.android.compose.screen.molecule.PupperPicsService
import com.cmgapps.android.compose.screen.molecule.pupperPicsPresenter
import kotlinx.coroutines.flow.Flow

class PupperPicsViewModel(
    private val service: PupperPicsService,
) : MoleculeViewModel<Event, Model>() {
    @Composable
    override fun models(events: Flow<Event>): Model = pupperPicsPresenter(events, service)

    companion object {
        val Factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PupperPicsViewModel(
                        PupperPicsService(),
                    )
                }
            }
    }
}
