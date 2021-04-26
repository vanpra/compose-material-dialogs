package com.vanpra.composematerialdialogdemos.demos

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import com.vanpra.composematerialdialogdemos.DialogAndShowButton
import com.vanpra.composematerialdialogdemos.R
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.iconTitle
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.title

/**
 * @brief Basic Dialog Demos
 */
@Composable
fun BasicDialogDemo() {
    DialogAndShowButton(buttonText = "Basic Dialog") {
        title(res = R.string.location_dialog_title)
        message(res = R.string.location_dialog_message)
    }

    DialogAndShowButton(buttonText = "Basic Dialog With Buttons") {
        title(res = R.string.location_dialog_title)
        message(res = R.string.location_dialog_message)
        buttons {
            negativeButton("Disagree")
            positiveButton("Agree")
        }
    }

    DialogAndShowButton(buttonText = "Basic Dialog With Buttons and Icon Title") {
        iconTitle(
            icon = {
                Image(
                    Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            },
            textRes = R.string.location_dialog_title
        )
        message(res = R.string.location_dialog_message)
        buttons {
            negativeButton("Disagree")
            positiveButton("Agree")
        }
    }

    DialogAndShowButton(buttonText = "Basic Dialog With Stacked Buttons") {
        title(res = R.string.location_dialog_title)
        message(res = R.string.location_dialog_message)
        buttons {
            negativeButton("No Thanks")
            positiveButton("Turn On Speed Boost")
        }
    }

    DialogAndShowButton(buttonText = "Basic Input Dialog") {
        title(res = R.string.input_dialog_title)
        input(label = "Name", hint = "Jon Smith") {
            Log.d("SELECTION:", it)
        }
        buttons {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    }

    DialogAndShowButton(buttonText = "Input Dialog with submit on IME Action") {
        title(res = R.string.input_dialog_title)
        input(
            label = "Name", hint = "Jon Smith",
            keyboardActions = KeyboardActions(
                onDone = { this@DialogAndShowButton.submit() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        ) {
            Log.d("SELECTION:", it)
        }
        buttons {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    }

    DialogAndShowButton(buttonText = "Input Dialog with input validation") {
        title("Please enter your email")
        input(
            label = "Email",
            hint = "hello@example.com",
            errorMessage = "Invalid email",
            isTextValid = {
                Patterns.EMAIL_ADDRESS.matcher(it).matches() && it.isNotEmpty()
            }
        ) {
            Log.d("SELECTION:", it)
        }

        buttons {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    }
}
