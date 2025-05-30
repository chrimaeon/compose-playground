package com.cmgapps.android.compose.screen

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.cmgapps.android.compose.route.SharedElementRoutes
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SharedElementTransitionScreenShould {
    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var navController: TestNavHostController

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Before
    fun setup() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            SharedTransitionLayout {
                SharedElementNavHost(
                    navController = navController,
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
            navController.currentBackStackEntry?.destination?.hasRoute<SharedElementRoutes.Details>(),
            `is`(true),
        )
    }
}
