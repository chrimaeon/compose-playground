package com.cmgapps.android.compose.screen.molecule

import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import app.cash.paparazzi.Paparazzi
import app.cash.turbine.Turbine
import app.cash.turbine.test
import com.cmgapps.android.compose.service.PupperPicsService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PupperPicsPresenterShould {
    private lateinit var service: FakePupperPicsService

    /**
     * Needed because of "Caused by: java.lang.UnsatisfiedLinkError: 'long android.os.Trace.nativeGetEnabledTags()'"
     *
     * See [https://github.com/cashapp/paparazzi/issues/1149](https://github.com/cashapp/paparazzi/issues/1149)
     */
    @get:Rule
    val paparazzi = Paparazzi()

    @Before
    fun setup() {
        service = FakePupperPicsService()
    }

    @Test
    fun `on launch, breeds a loaded followed by an image url`() =
        runTest {
            moleculeFlow(mode = RecompositionMode.Immediate) {
                pupperPicsPresenter(emptyFlow(), service)
            }.test {
                assertThat(
                    awaitItem(),
                    `is`(
                        Model(
                            loading = true,
                            breeds = emptyList(),
                            dropdownText = null,
                            currentUrl = null,
                        ),
                    ),
                )

                val breed1 = Breed("Breed 1", "breed1")
                val breeds = listOf(breed1, Breed("Breed 2", "breed2"))

                service.breeds.add(breeds)

                assertThat(
                    awaitItem(),
                    `is`(
                        Model(
                            loading = false,
                            breeds = breeds,
                            dropdownText = breeds.first().name,
                            currentUrl = null,
                        ),
                    ),
                )
                assertThat(service.urlRequestArgs.awaitItem(), `is`(breed1.urlPath))

                service.urls.add(breed1.urlPath)
                assertThat(
                    awaitItem(),
                    `is`(
                        Model(
                            loading = false,
                            breeds = breeds,
                            dropdownText = breeds.first().name,
                            currentUrl = breed1.urlPath,
                        ),
                    ),
                )
            }
        }

    @Test
    fun `selecting breed updates dropdown text and fetches new image`() =
        runTest {
            val events = Channel<Event>()

            moleculeFlow(mode = RecompositionMode.Immediate) {
                pupperPicsPresenter(events.receiveAsFlow(), service)
            }.test {
                val breed1 = Breed("Breed 1", "breed1")
                val breed3 = Breed("Breed 3", "breed3")
                val breeds =
                    listOf(
                        breed1,
                        Breed("Breed 2", "breed2"),
                        breed3,
                    )
                service.breeds.add(breeds)
                service.urls.add(breed1.urlPath)
                assertThat(service.urlRequestArgs.awaitItem(), `is`(breed1.urlPath))
                skipItems(3) // Fetching list, fetching fetching url, resolved model.

                events.send(Event.SelectBreed(breed3))
                assertThat(
                    awaitItem(),
                    `is`(
                        Model(
                            loading = false,
                            breeds = breeds,
                            dropdownText = breed3.name,
                            currentUrl = breed1.urlPath,
                        ),
                    ),
                )
                assertThat(
                    awaitItem(),
                    `is`(
                        Model(
                            loading = false,
                            breeds = breeds,
                            dropdownText = breed3.name,
                            currentUrl = null,
                        ),
                    ),
                )

                assertThat(service.urlRequestArgs.awaitItem(), `is`(breed3.urlPath))
                service.urls.add(breed3.urlPath)

                assertThat(
                    awaitItem(),
                    `is`(
                        Model(
                            loading = false,
                            breeds = breeds,
                            dropdownText = breed3.name,
                            currentUrl = breed3.urlPath,
                        ),
                    ),
                )
            }
        }
}

private class FakePupperPicsService : PupperPicsService {
    val breeds = Turbine<List<Breed>>()
    val urls = Turbine<String>()
    val urlRequestArgs = Turbine<String>()

    override suspend fun listBreeds(): List<Breed> = breeds.awaitItem()

    override suspend fun randomImageUrlFor(breed: String): String {
        urlRequestArgs.add(breed)
        return urls.awaitItem()
    }
}
