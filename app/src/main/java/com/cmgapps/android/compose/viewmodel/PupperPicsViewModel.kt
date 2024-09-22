/*
 * Copyright (c) 2024. Christian Grach <christian.grach@cmgapps.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.cmgapps.android.compose.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.cash.molecule.RecompositionMode
import com.cmgapps.android.compose.screen.molecule.Event
import com.cmgapps.android.compose.screen.molecule.Model
import com.cmgapps.android.compose.screen.molecule.pupperPicsPresenter
import com.cmgapps.android.compose.service.PupperPicsService
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class PupperPicsViewModel(
    private val service: PupperPicsService,
    mainContext: CoroutineContext = AndroidUiDispatcher.Main,
    mode: RecompositionMode = RecompositionMode.ContextClock,
) : MoleculeViewModel<Event, Model>(mainContext, mode) {
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
