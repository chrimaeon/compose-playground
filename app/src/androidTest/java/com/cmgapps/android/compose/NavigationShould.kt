package com.cmgapps.android.compose

import androidx.activity.ComponentActivity
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.cmgapps.android.compose.screen.Dashboard
import com.cmgapps.android.compose.screen.TimePickerScreen
import com.cmgapps.android.compose.ui.theme.Theme
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
class NavigationShould {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

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

    @Test
    fun navigateToMolecule() {
        mockWebServer.dispatcher =
            object : Dispatcher() {
                override fun dispatch(request: RecordedRequest): MockResponse =
                    when (request.path) {
                        "/api/breeds/list/all" -> {
                            MockResponse()
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .setBody(
                                    """
                                    {
                                        "message": {
                                            "bulldog": [],
                                            "labrador": [],
                                            "poodle": []
                                        },
                                        "status": "success"
                                    }
                                    """.trimIndent(),
                                )
                        }

                        "/api/breed/bulldog/images/random" -> {
                            MockResponse()
                                .addHeader("Content-Type", "application/json; charset=utf-8")
                                .setBody(
                                    """
                                    {
                                        "message": "https://example.com/bulldog.jpg",
                                        "status": "success"
                                    }
                                    """.trimIndent(),
                                )
                        }

                        else -> MockResponse().setResponseCode(404)
                    }
            }

        mockWebServer.start()

        val baseUrl = mockWebServer.url("/api/").toString()

        composeTestRule.setContent {
            Theme {
                Dashboard(dogCeoServerBaseUrl = baseUrl)
            }
        }

        val label = composeTestRule.activity.getString(R.string.molecule)
        composeTestRule.onNodeWithText(label).assertExists().performClick()
        composeTestRule.onNodeWithTag("MoleculeScreen").assertIsDisplayed()
    }

    @Test
    fun navigateToAnimateItem() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }

        val lable = composeTestRule.activity.getString(R.string.animate_item)
        composeTestRule.onNodeWithText(lable).assertExists().performClick()
        composeTestRule.onNodeWithTag("AnimateItemScreen").assertIsDisplayed()
    }

    @Test
    fun navigateToTextFieldTransformation() {
        composeTestRule.setContent {
            Theme {
                Dashboard()
            }
        }

        val lable = composeTestRule.activity.getString(R.string.textfield_transformation)
        composeTestRule.onNodeWithText(lable).assertExists().performClick()
        composeTestRule.onNodeWithTag("TextFieldTransformationScreen").assertIsDisplayed()
    }
}
