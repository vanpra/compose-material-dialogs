package com.vanpra.composematerialdialogs.test.utils.extensions

import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import java.util.Locale

enum class ColorPickerSlider {
    Alpha,
    Red,
    Blue,
    Green
}

fun ComposeTestRule.onDialogColorPicker() =
    this.onNodeWithTag("dialog_color_picker")

fun ComposeTestRule.onDialogColorSelector(index: Int) =
    this.onNodeWithTag("dialog_color_selector_$index")

fun ComposeTestRule.onDialogSubColorSelector(index: Int) =
    this.onNodeWithTag("dialog_sub_color_selector_$index")

fun ComposeTestRule.onDialogSubColorBackButton() =
    this.onNodeWithTag("dialog_sub_color_back_btn")

fun ComposeTestRule.onDialogColorSlider(slider: ColorPickerSlider) =
    this.onNodeWithTag("dialog_color_picker_${slider.toString().lowercase(Locale.ROOT)}_slider")
