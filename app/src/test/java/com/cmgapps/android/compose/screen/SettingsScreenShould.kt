package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test

class SettingsScreenShould : PaparazziTest() {
    @Test
    fun `render screen`() {
        paparazzi.snapshot {
            SettingsScreen(backButton = {}, buildYear = "2024")
        }
    }
}
