package com.cmgapps.android.compose.screen

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.Posture
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.window.core.layout.WindowSizeClass
import app.cash.paparazzi.DeviceConfig
import com.android.resources.ScreenOrientation
import com.cmgapps.android.compose.route.SubRoutes
import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
class DashboardShould : PaparazziTest() {
    @Test
    fun `render dashboard`() {
        paparazzi.snapshot {
            Dashboard()
        }
    }

    @Test
    fun `render list and detail on large screens`() {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = DeviceConfig.NEXUS_10.copy(orientation = ScreenOrientation.LANDSCAPE),
        )

        paparazzi.snapshot {
            val scaffoldNavigator =
                rememberListDetailPaneScaffoldNavigator<SubRoutes>(
                    // Hack for ListDetailPane not getting the correct values
                    calculatePaneScaffoldDirective(
                        WindowAdaptiveInfo(
                            WindowSizeClass.compute(
                                DeviceConfig.NEXUS_10.screenHeight.toFloat(),
                                DeviceConfig.NEXUS_10.screenWidth.toFloat(),
                            ),
                            Posture(),
                        ),
                    ),
                )
            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, SubRoutes.ChipTextField)

            Dashboard(scaffoldNavigator = scaffoldNavigator)
        }
    }
}
