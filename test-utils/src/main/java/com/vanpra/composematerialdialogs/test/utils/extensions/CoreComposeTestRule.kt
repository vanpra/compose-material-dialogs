package com.vanpra.composematerialdialogs.test.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipeUp

fun ComposeContentTestRule.setContentAndWaitForIdle(content: @Composable () -> Unit) {
    this.setContent {
        content()
    }
    this.waitForIdle()
}

fun ComposeTestRule.onPositiveButton() =
    this.onNodeWithTag("positive")

fun ComposeTestRule.onNegativeButton() =
    this.onNodeWithTag("negative")

fun ComposeTestRule.onDialog() =
    this.onNodeWithTag("dialog")

fun ComposeTestRule.assertDialogExists() =
    this.onDialog().assertExists()

fun ComposeTestRule.assertDialogDoesNotExist() =
    this.onDialog().assertDoesNotExist()

fun ComposeTestRule.onDialogList() =
    this.onNodeWithTag("dialog_list")

fun ComposeTestRule.onDialogListItem(index: Int): SemanticsNodeInteraction {
    try {
        onNodeWithTag("dialog_list_item_$index").assertExists()
    } catch (e: AssertionError) {
        onDialogList().performGesture { swipeUp() }
        waitForIdle()
    }

    return onNodeWithTag("dialog_list_item_$index").assertExists()
}

fun ComposeTestRule.onDialogInput() =
    this.onNodeWithTag("dialog_input")

fun ComposeTestRule.onDialogInputError() =
    this.onNodeWithTag("dialog_input_error")
