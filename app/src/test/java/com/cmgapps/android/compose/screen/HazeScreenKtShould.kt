package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test
import kotlin.random.Random

class HazeScreenKtShould : PaparazziTest() {
    @Test
    fun `render haze screen`() {
        paparazzi.snapshot {
            HazeScreen(backButton = {}, random = Random(0))
        }
    }
}
