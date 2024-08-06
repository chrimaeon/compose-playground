package com.cmgapps.android.compose.viewmodel

import com.cmgapps.android.compose.toLocalTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TimePickerViewModelShould {
    private lateinit var viewmodel: TimePickerViewModel

    private val now = Instant.parse("1979-09-02T13:45:12Z")

    private val localTimeZone = TimeZone.UTC

    @BeforeEach
    fun setUp() {
        viewmodel =
            TimePickerViewModel(
                clock =
                    object : Clock {
                        override fun now(): Instant = now
                    },
            )
    }

    @Test
    fun `handle the selected time`() {
        val selectedTime = viewmodel.selectedTime
        assertThat(selectedTime, `is`(now.toLocalTime()))
    }

    @Test
    fun `update the selected time`() {
        val updatedTime = LocalTime(15, 30)
        viewmodel.updateTime(updatedTime)
        val selectedTime = viewmodel.selectedTime
        assertThat(selectedTime, `is`(updatedTime))
    }
}
