package com.vanpra.composematerialdialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 *  Adds a title with the given text to the dialog
 * @param text title text from a string literal
 * @param res title text from a string resource
 * @param center text is aligned to center when true
 */
@Composable
fun MaterialDialog.title(
    text: String? = null,
    @StringRes res: Int? = null,
    center: Boolean = false
) {
    val titleText = getString(res, text)
    var modifier = Modifier
        .fillMaxWidth()
        .padding(start = 24.dp, end = 24.dp)
        .height(64.dp)
        .wrapContentHeight(Alignment.CenterVertically)

    modifier = modifier.then(
        Modifier.wrapContentWidth(
            if (center) {
                Alignment.CenterHorizontally
            } else {
                Alignment.Start
            }
        )
    )

    Text(
        text = titleText,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.h6,
        modifier = modifier
    )
}

/**
 *  Adds a title with the given text and icon to the dialog
 * @param text title text from a string literal
 * @param textRes title text from a string resource
 * @param icon optional icon displayed at the start of the title
 */
@Composable
fun MaterialDialog.iconTitle(
    text: String? = null,
    @StringRes textRes: Int? = null,
    icon: @Composable () -> Unit = {},
) {
    val titleText = getString(textRes, text)
    Row(
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon()
        Spacer(Modifier.width(14.dp))
        Text(
            text = titleText,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h6
        )
    }
}

/**
 *  Adds paragraph of text to the dialog
 * @param text message text from a string literal
 * @param res message text from a string resource
 */
@Composable
fun MaterialDialog.message(text: String? = null, @StringRes res: Int? = null) {
    val messageText = getString(res, text)

    Text(
        text = messageText,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.body1,
        modifier = Modifier
            .padding(bottom = 28.dp, start = 24.dp, end = 24.dp)
    )
}

/**
 *  Adds an input field with the given parameters to the dialog
 * @param label string to be shown in the input field before selection eg. Username
 * @param hint hint to be shown in the input field when it is selected but empty eg. Joe
 * @param prefill string to be input into the text field by default
 * @param waitForPositiveButton if true the [onInput] callback will only be called when the
 * positive button is pressed, otherwise it will be called when the input value is changed
 * @param visualTransformation a visual transformation of the content of the text field
 * @param keyboardOptions software keyboard options which can be used to customize parts
 * of the keyboard
 * @param errorMessage a message to be shown to the user when the input is not valid
 * @param focusRequester a [FocusRequester] which can be used to control the focus state of the
 * text field
 * @param focusOnShow if set to true this will auto focus the text field when the input
 * field is shown
 * @param isTextValid a function which is called to check if the user input is valid
 * @param onInput a function which is called with the user input. The timing of this call is
 * dictated by [waitForPositiveButton]
 */
@Composable
fun MaterialDialog.input(
    label: String,
    hint: String = "",
    prefill: String = "",
    waitForPositiveButton: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    errorMessage: String = "",
    focusRequester: FocusRequester = FocusRequester.Default,
    focusOnShow: Boolean = false,
    isTextValid: (String) -> Boolean = { true },
    onInput: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf(prefill) }
    val valid = remember(text) { isTextValid(text) }
    val focusManager = LocalFocusManager.current

    val positiveEnabledIndex = addPositiveButtonEnabled(valid = valid) {
        focusManager.clearFocus()
    }

    DisposableEffect(valid) {
        setPositiveEnabled(positiveEnabledIndex, valid)
        onDispose { }
    }

    if (waitForPositiveButton) {
        DialogCallback { onInput(text) }
    }

    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                if (!waitForPositiveButton) {
                    onInput(text)
                }
            },
            label = { Text(label, color = MaterialTheme.colors.onBackground.copy(0.8f)) },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
                .testTag("dialog_input"),
            placeholder = { Text(hint, color = MaterialTheme.colors.onBackground.copy(0.5f)) },
            isError = !valid,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = TextStyle(MaterialTheme.colors.onBackground, fontSize = 16.sp)
        )

        if (!valid) {
            Text(
                errorMessage,
                fontSize = 14.sp,
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.End).testTag("dialog_input_error")
            )
        }
    }

    if (focusOnShow) {
        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }
    }
}

/**
 * Create an view in the dialog with the given content and appropriate padding
 * @param content the content of the custom view
 */
@Composable
fun MaterialDialog.customView(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(bottom = 28.dp, start = 24.dp, end = 24.dp)) {
        content()
    }
}
