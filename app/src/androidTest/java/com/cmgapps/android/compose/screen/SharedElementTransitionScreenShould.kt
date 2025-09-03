package com.cmgapps.android.compose.screen

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.cmgapps.android.compose.route.SharedElementRoutes
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedElementTransitionScreenShould {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var backStack: NavBackStack<NavKey>

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Before
    fun setup() {
        composeTestRule.setContent {
            backStack = rememberNavBackStack(SharedElementRoutes.Main)

            SharedTransitionLayout {
                SharedElementNavHost(
                    backstack = backStack,
                    cupcakes = List(8) { Cupcake(it) },
                    imageBoundsTransform = { _, _ ->
                        spring(
                            stiffness = StiffnessMediumLow,
                            visibilityThreshold = Rect.VisibilityThreshold,
                        )
                    },
                    backButton = {},
                )
            }
        }
    }

    @Test
    fun showStartDestination() {
        composeTestRule
            .onNodeWithTag("SharedElementTransitionMainContent")
            .assertIsDisplayed()
    }

    @Test
    fun navigateToDetails() {
        composeTestRule.onAllNodesWithTag("CupcakeCard")[0].assertExists().performClick()
        assertThat(
            backStack,
            contains(SharedElementRoutes.Main, SharedElementRoutes.Details(0)),
        )
    }
}
