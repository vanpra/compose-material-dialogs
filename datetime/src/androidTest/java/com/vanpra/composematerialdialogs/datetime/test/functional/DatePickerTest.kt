package com.vanpra.composematerialdialogs.datetime.test.functional

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.test.utils.DialogWithContent
import com.vanpra.composematerialdialogs.test.utils.defaultButtons
import com.vanpra.composematerialdialogs.test.utils.extensions.assertDialogDoesNotExist
import com.vanpra.composematerialdialogs.test.utils.extensions.onDialogDateSelector
import com.vanpra.composematerialdialogs.test.utils.extensions.onPositiveButton
import com.vanpra.composematerialdialogs.title
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class DatePickerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testDate = LocalDate.of(2021, 1, 1)

    @Test
    fun datePickerDialogWaitForPositiveButton() {
        var selectedDate: LocalDate? = null

        composeTestRule.setContent {
            DialogWithContent {
                datepicker(initialDate = testDate, waitForPositiveButton = true) {
                    selectedDate = it
                }
                defaultButtons()
            }
        }

        composeTestRule.onDialogDateSelector(20).performClick()
        assertEquals(null, selectedDate)
        composeTestRule.onPositiveButton().performClick()
        /* Need this line or else tests don't wait for dialog to close */
        composeTestRule.assertDialogDoesNotExist()
        assertEquals(LocalDate.of(2021, 1, 20), selectedDate)
    }

    @Test
    fun datePickerDialogDontWaitForPositiveButton() {
        var selectedDate: LocalDate? = null

        composeTestRule.setContent {
            DialogWithContent {
                datepicker(initialDate = testDate, waitForPositiveButton = false) {
                    selectedDate = it
                }
                defaultButtons()
            }
        }

        composeTestRule.onDialogDateSelector(20).performClick()
        composeTestRule.waitForIdle()
        assertEquals(LocalDate.of(2021, 1, 20), selectedDate)
        selectedDate = null
        composeTestRule.onPositiveButton().performClick()
        /* Need this line or else tests don't wait for dialog to close */
        composeTestRule.assertDialogDoesNotExist()
        assertEquals(null, selectedDate)
    }

    @Test
    fun datePickerCustomTitle() {
        val title = "Custom Title"
        composeTestRule.setContent {
            DialogWithContent {
                datepicker(title = title)
                defaultButtons()
            }
        }

        composeTestRule.onNodeWithText(title).assertExists()
    }
}
