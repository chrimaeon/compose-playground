package com.cmgapps.android.compose

import androidx.activity.ComponentActivity
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.cmgapps.android.compose.screen.Dashboard
import com.cmgapps.android.compose.screen.SharedElementTransitionScreen
import com.cmgapps.android.compose.screen.TimePickerScreen
import com.cmgapps.android.compose.ui.theme.Theme
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
class NavigationShould {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun navigateToChipTextField() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }

        val label = composeTestRule.activity.getString(R.string.chip_text_field)

        composeTestRule.onNodeWithText(label).assertExists().performClick()
        composeTestRule.onNodeWithTag("ChipTextField").assertIsDisplayed()
    }

    @Test
    fun navigateToTimePicker() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }

        val label = composeTestRule.activity.getString(R.string.time_picker)
        composeTestRule.onNodeWithText(label).assertExists().performClick()
        val pickATime = composeTestRule.activity.getString(R.string.pick_a_time)
        composeTestRule.onNodeWithContentDescription(pickATime).assertIsDisplayed()
    }

    @Test
    fun openTimePickerDialog() {
        composeTestRule.setContent {
            Theme {
                TimePickerScreen(backButton = {})
            }
        }

        val pickATimeButtonLabel = composeTestRule.activity.getString(R.string.pick_a_time)
        composeTestRule.onNodeWithContentDescription(pickATimeButtonLabel).assertExists().performClick()

        composeTestRule.onNode(isDialog()).printToLog("DialogShould")
        composeTestRule
            .onNode(isDialog() and hasAnyChild(hasTestTag("TimePickerDialog")))
            .assertIsDisplayed()
    }

    @Test
    fun navigateToSharedElementTransition() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }

        val label = composeTestRule.activity.getString(R.string.shared_element_transition)
        composeTestRule.onNodeWithText(label).assertExists().performClick()
        composeTestRule.onNodeWithTag("SharedElementTransitionMainContent").assertIsDisplayed()
    }

    @Test
    fun navigateToSharedElement() {
        composeTestRule.setContent {
            Theme {
                SharedElementTransitionScreen()
            }
        }

        composeTestRule.onAllNodesWithTag("CupcakeCard")[0].assertExists().performClick()
        composeTestRule.onNodeWithTag("SharedElementTransitionDetailsContent").assertIsDisplayed()
    }

    @Test
    fun navigateToReveal() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }

        val label = composeTestRule.activity.getString(R.string.reveal)
        composeTestRule.onNodeWithText(label).assertExists().performClick()
        val stepOne = composeTestRule.activity.getString(R.string.step_one)
        val stepTwo = composeTestRule.activity.getString(R.string.step_two)
        val stepThree = composeTestRule.activity.getString(R.string.step_three)

        composeTestRule.onNodeWithText(stepOne).assertIsDisplayed()
        composeTestRule.onNodeWithText(stepTwo).assertIsDisplayed()
        composeTestRule.onNodeWithText(stepThree).assertIsDisplayed()
    }

    @Test
    fun navigateToParallaxScrolling() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }

        val label = composeTestRule.activity.getString(R.string.parallax_scrolling)
        composeTestRule.onNodeWithText(label).assertExists().performClick()
        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()
    }

    @Test
    fun navigateToHaze() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }
        val label = composeTestRule.activity.getString(R.string.haze)
        composeTestRule.onNodeWithText(label).assertExists().performClick()
        composeTestRule.onNodeWithTag("HazeScreen").assertIsDisplayed()
    }

    @Test
    fun navigateToPullToRefresh() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }
        val label = composeTestRule.activity.getString(R.string.pull_2_refresh)
        composeTestRule.onNodeWithText(label).assertExists().performClick()
        composeTestRule.onNodeWithTag("PullToRefreshScreen").assertIsDisplayed()
    }
}
