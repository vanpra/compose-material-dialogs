package com.vanpra.composematerialdialogs.datetime.test.screenshot

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.karumi.shot.ScreenshotTest
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.datetimepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.test.utils.DialogWithContent
import com.vanpra.composematerialdialogs.test.utils.extensions.onDialog
import com.vanpra.composematerialdialogs.test.utils.extensions.onDialogDateNextMonth
import com.vanpra.composematerialdialogs.test.utils.extensions.onDialogDateSelector
import com.vanpra.composematerialdialogs.test.utils.extensions.setContentAndWaitForIdle
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DateTimePickerTest : ScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val testTitle = "Custom Title"

    @Test
    fun datePickerBasic() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                datepicker(initialDate = LocalDate.of(2021, 1, 1))
            }
        }
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun timePickerBasic() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                timepicker(initialTime = LocalTime.of(12, 0))
            }
        }
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun timePicker24Hour() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                timepicker(
                    initialTime = LocalTime.of(12, 0),
                    is24HourClock = true
                )
            }
        }
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun datetimePickerBasic() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                datetimepicker(
                    initialDateTime = LocalDateTime.of(2021, 1, 1, 12, 0)
                )
            }
        }
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun datetimePickerSelectionOnNextMonth() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                datetimepicker(
                    initialDateTime = LocalDateTime.of(2021, 1, 1, 12, 0)
                )
            }
        }

        composeTestRule.onDialogDateNextMonth().performClick()
        composeTestRule.onDialogDateSelector(3).performClick()
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun datePickerWithCustomTitle() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                datepicker(title = testTitle)
            }
        }

        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun timePickerWithCustomTitle() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                timepicker(title = testTitle)
            }
        }

        compareScreenshot(composeTestRule.onDialog())
    }
}
