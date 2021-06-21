package com.vanpra.composematerialdialogs.test.functional

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.test.utils.DialogWithContent
import com.vanpra.composematerialdialogs.test.utils.extensions.assertDialogDoesNotExist
import com.vanpra.composematerialdialogs.test.utils.extensions.assertDialogExists
import com.vanpra.composematerialdialogs.test.utils.extensions.onNegativeButton
import com.vanpra.composematerialdialogs.test.utils.extensions.onPositiveButton
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DialogButtonsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    private fun MaterialDialog.defaultButtons() {
        buttons {
            negativeButton("Disagree")
            positiveButton("Agree")
        }
    }

    @Test
    fun dialogDismissedOnPositiveButton() {
        composeTestRule.setContent { DialogWithContent { defaultButtons() } }
        composeTestRule.onPositiveButton().performClick()
        composeTestRule.assertDialogDoesNotExist()
    }

    @Test
    fun dialogDismissedOnNegativeButton() {
        composeTestRule.setContent { DialogWithContent { defaultButtons() } }
        composeTestRule.onNegativeButton().performClick()
        composeTestRule.assertDialogDoesNotExist()
    }

    @Test
    fun dialogNotDismissedOnPositiveButton() {
        composeTestRule.setContent { DialogWithContent(false) { defaultButtons() } }
        composeTestRule.onPositiveButton().performClick()
        composeTestRule.assertDialogExists()
    }

    @Test
    fun dialogNotDismissedOnNegativeButton() {
        composeTestRule.setContent { DialogWithContent(false) { defaultButtons() } }
        composeTestRule.onNegativeButton().performClick()
        composeTestRule.assertDialogExists()
    }
}
