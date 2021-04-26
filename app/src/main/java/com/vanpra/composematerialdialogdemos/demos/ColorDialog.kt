package com.vanpra.composematerialdialogdemos.demos

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogdemos.DialogAndShowButton
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.color.ARGBPickerState
import com.vanpra.composematerialdialogs.color.ColorPalette
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.title

/**
 * @brief Color Picker Demos
 */
@Composable
fun ColorDialogDemo() {
    var waitForPositiveButton by remember { mutableStateOf(false) }

    Row(Modifier.padding(8.dp)) {
        Switch(
            checked = waitForPositiveButton,
            onCheckedChange = { waitForPositiveButton = it }
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "Wait for positive button",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onBackground
        )
    }

    DialogAndShowButton(buttonText = "Color Picker Dialog") {
        title("Select a Color")
        colorChooser(colors = ColorPalette.Primary, waitForPositiveButton = waitForPositiveButton) {
            println(it)
        }

        defaultButtons()
    }

    DialogAndShowButton(buttonText = "Color Picker Dialog With Sub Colors") {
        title("Select a Sub Color")
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            waitForPositiveButton = waitForPositiveButton
        ) {
            println(it)
        }

        defaultButtons()
    }

    DialogAndShowButton(buttonText = "Color Picker Dialog With Initial Selection") {
        title("Select a Sub Color")
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            waitForPositiveButton = waitForPositiveButton,
            initialSelection = 5
        ) {
            println(it)
        }
        defaultButtons()
    }

    DialogAndShowButton(buttonText = "Color Picker Dialog With RGB Selector") {
        title("Custom RGB")
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            argbPickerState = ARGBPickerState.WithoutAlphaSelector,
            waitForPositiveButton = waitForPositiveButton
        ) {
            println(it)
        }
        defaultButtons()
    }

    DialogAndShowButton(buttonText = "Color Picker Dialog With ARGB Selector") {
        title("Custom ARGB")
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            argbPickerState = ARGBPickerState.WithAlphaSelector,
            waitForPositiveButton = waitForPositiveButton
        ) {
            println(it)
        }
        defaultButtons()
    }
}

@Composable
private fun MaterialDialog.defaultButtons() {
    buttons {
        positiveButton("Select")
        negativeButton("Cancel")
    }
}
