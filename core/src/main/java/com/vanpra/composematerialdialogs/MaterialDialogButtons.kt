package com.vanpra.composematerialdialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import java.util.Locale

internal enum class MaterialDialogButtonTypes(val testTag: String) {
    Text("text"),
    Positive("positive"),
    Negative("negative"),
    Accessibility("accessibility")
}

/**
 *  Adds buttons to the bottom of the dialog
 * @param content the buttons which should be displayed in the dialog.
 * See [MaterialDialogButtons] for more information about the content
 */
@Composable
fun MaterialDialog.buttons(content: @Composable MaterialDialogButtons.() -> Unit) {
    val interButtonPadding = with(LocalDensity.current) { 12.dp.toPx().toInt() }
    val defaultBoxHeight = with(LocalDensity.current) { 36.dp.toPx().toInt() }
    val accessibilityPadding = with(LocalDensity.current) { 12.dp.toPx().toInt() }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
            .layoutId("buttons")
    ) {
        Layout(
            { content(buttons) }, Modifier,
            { measurables, constraints ->
                val placeables = measurables.map {
                    (it.layoutId as MaterialDialogButtonTypes) to it.measure(constraints)
                }
                val totalWidth = placeables.map { it.second.width }.sum()
                val column = totalWidth > 0.8 * constraints.maxWidth

                val height =
                    if (column) {
                        val buttonHeight = placeables.map { it.second.height }.sum()
                        val heightPadding = (placeables.size - 1) * interButtonPadding
                        buttonHeight + heightPadding
                    } else {
                        defaultBoxHeight
                    }

                layout(constraints.maxWidth, height) {
                    var currX = constraints.maxWidth
                    var currY = 0

                    val posButtons = placeables.buttons(MaterialDialogButtonTypes.Positive)
                    val negButtons = placeables.buttons(MaterialDialogButtonTypes.Negative)
                    val textButtons = placeables.buttons(MaterialDialogButtonTypes.Text)
                    val accButtons = placeables.buttons(MaterialDialogButtonTypes.Accessibility)

                    val buttonInOrder = posButtons + textButtons + negButtons
                    buttonInOrder.forEach { button ->
                        if (column) {
                            button.place(currX - button.width, currY)
                            currY += button.height + interButtonPadding
                        } else {
                            currX -= button.width
                            button.place(currX, 0)
                        }
                    }

                    if (accButtons.isNotEmpty()) {
                        /* There can only be one accessibility button so take first */
                        val button = accButtons[0]
                        button.place(accessibilityPadding, height - button.height)
                    }
                }
            }
        )
    }
}

/**
 * A class used to build a buttons layout for a MaterialDialog. This should be used in conjunction
 * with the [com.vanpra.composematerialdialogs.MaterialDialog.buttons] function
 */
class MaterialDialogButtons(private val dialog: MaterialDialog) {
    /**
     * Adds a button which is always enabled to the bottom of the dialog. This should
     * only be used for neutral actions.
     *
     * @param text the string literal text shown in the button
     * @param res the string resource text shown in the button
     * @param onClick a callback which is called when the button is pressed
     */
    @Composable
    fun button(
        text: String? = null,
        textStyle: TextStyle = MaterialTheme.typography.button,
        @StringRes res: Int? = null,
        onClick: () -> Unit = {}
    ) {
        val buttonText = getString(res, text).uppercase(Locale.ROOT)
        TextButton(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .layoutId(MaterialDialogButtonTypes.Text)
                .testTag(MaterialDialogButtonTypes.Text.testTag),
        ) {
            Text(text = buttonText, style = textStyle)
        }
    }

    /**
     * Adds a positive button to the dialog
     *
     * @param text the string literal text shown in the button
     * @param res the string resource text shown in the button
     * @param disableDismiss when true this will stop the dialog closing when the button is pressed
     * even if autoDismissing is disabled
     * @param onClick a callback which is called when the button is pressed
     */
    @Composable
    fun positiveButton(
        text: String? = null,
        textStyle: TextStyle = MaterialTheme.typography.button,
        @StringRes res: Int? = null,
        disableDismiss: Boolean = false,
        onClick: () -> Unit = {}
    ) {
        val buttonText = getString(res, text).uppercase(Locale.ROOT)
        val buttonEnabled = dialog.positiveEnabled.values.all { it }
        val focusManager = LocalFocusManager.current

        TextButton(
            onClick = {
                if (dialog.isAutoDismiss() && !disableDismiss) {
                    dialog.hide(focusManager)
                }

                dialog.callbacks.values.forEach {
                    it()
                }

                onClick()
            },
            modifier = Modifier.layoutId(MaterialDialogButtonTypes.Positive)
                .testTag(MaterialDialogButtonTypes.Positive.testTag),
            enabled = buttonEnabled && dialog.positiveButtonEnabledOverride
        ) {
            Text(text = buttonText, style = textStyle)
        }
    }

    /**
     * Adds a negative button to the dialog
     *
     * @param text the string literal text shown in the button
     * @param res the string resource text shown in the button
     * even if autoDismissing is disabled
     * @param onClick a callback which is called when the button is pressed
     */
    @Composable
    fun negativeButton(
        text: String? = null,
        textStyle: TextStyle = MaterialTheme.typography.button,
        @StringRes res: Int? = null,
        onClick: () -> Unit = {}
    ) {
        val buttonText = getString(res, text).uppercase(Locale.ROOT)
        val focusManager = LocalFocusManager.current

        TextButton(
            onClick = {
                if (dialog.isAutoDismiss()) {
                    dialog.hide(focusManager)
                }
                onClick()
            },
            modifier = Modifier.layoutId(MaterialDialogButtonTypes.Negative)
                .testTag(MaterialDialogButtonTypes.Negative.testTag),
        ) {
            Text(text = buttonText, style = textStyle)
        }
    }

    /**
     * Adds a accessibility button to the bottom left of the dialog
     *
     * @param icon the icon to be shown on the button
     * @param onClick a callback which is called when the button is pressed
     */
    @Composable
    fun accessibilityButton(
        icon: ImageVector,
        colorFilter: ColorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
        onClick: () -> Unit
    ) {
        Box(
            Modifier
                .size(48.dp)
                .layoutId(MaterialDialogButtonTypes.Accessibility)
                .testTag(MaterialDialogButtonTypes.Accessibility.testTag)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Image(
                icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                colorFilter = colorFilter
            )
        }
    }
}
