package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Ignore
import org.junit.Test
import kotlin.random.Random

class HazeScreenKtShould : PaparazziTest() {
    @Ignore("Software rendering doesn't support drawRenderNode")
    @Test
    fun `render haze screen`() {
        paparazzi.snapshot {
            HazeScreen(backButton = {}, random = Random(0))
        }
    }
}
