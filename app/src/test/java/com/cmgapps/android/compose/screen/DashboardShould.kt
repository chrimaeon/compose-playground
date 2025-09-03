package com.cmgapps.android.compose.screen

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import com.cmgapps.android.compose.test.PaparazziTest
import com.cmgapps.android.compose.viewmodel.NavigationViewModel
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
class DashboardShould : PaparazziTest() {
    private lateinit var navigationViewModel: NavigationViewModel

    private val navigationEventDispatcherOwner =
        object : NavigationEventDispatcherOwner {
            override val navigationEventDispatcher = NavigationEventDispatcher()
        }

    @Before
    fun setup() {
        navigationViewModel = NavigationViewModel()
    }

    @Test
    @Ignore("Paparazzi does not work with AGP9.x")
    fun `render dashboard`() {
        // paparazzi.snapshot {
        //     CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides navigationEventDispatcherOwner) {
        //         Dashboard(navigationViewModel = navigationViewModel)
        //     }
        // }
    }

    @Test
    @Ignore("Paparazzi does not work with AGP9.x")
    fun `render list and detail on large screens`() {
        // paparazzi.unsafeUpdateConfig(
        //     deviceConfig = DeviceConfig.NEXUS_10.copy(orientation = ScreenOrientation.LANDSCAPE),
        // )
        //
        // val navigationViewModel = NavigationViewModel()
        // navigationViewModel.push(SubRoutes.ChipTextField)
        //
        // paparazzi.snapshot {
        //     CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides navigationEventDispatcherOwner) {
        //         Dashboard(navigationViewModel = navigationViewModel)
        //     }
        // }
    }
}
