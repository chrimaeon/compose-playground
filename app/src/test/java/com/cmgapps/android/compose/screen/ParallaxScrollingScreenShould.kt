package com.cmgapps.android.compose.screen

import com.cmgapps.android.compose.test.PaparazziTest
import org.junit.Test

class ParallaxScrollingScreenShould : PaparazziTest() {
    @Test
    fun `render parallax scrolling`() {
        paparazzi.snapshot {
            ParallaxScrollingScreen(
                backButton = {},
            )
        }
    }
}
