package com.vanpra.composematerialdialogs.datetime.test.functional

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.test.utils.DialogWithContent
import com.vanpra.composematerialdialogs.test.utils.defaultButtons
import org.junit.Rule
import org.junit.Test

class TimePickerTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun timePickerCustomTitle() {
        val title = "Custom Title"
        composeTestRule.setContent {
            DialogWithContent {
                timepicker(title = title)
                defaultButtons()
            }
        }

        composeTestRule.onNodeWithText(title).assertExists()
    }
}
