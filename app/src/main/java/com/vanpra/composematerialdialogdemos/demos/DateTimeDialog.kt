package com.vanpra.composematerialdialogdemos.demos

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.vanpra.composematerialdialogdemos.DialogAndShowButton
import com.vanpra.composematerialdialogs.buttons
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.datetimepicker
import com.vanpra.composematerialdialogs.datetime.time.TimePickerColors
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

/**
 * @brief Date and Time Picker Demos
 */
@Composable
fun DateTimeDialogDemo() {
    val purple = remember { Color(0xFF3700B3) }

    val colors: TimePickerColors = if (isSystemInDarkTheme()) {
        TimePickerDefaults.colors(
            activeBackgroundColor = purple.copy(0.3f),
            activeTextColor = Color.White,
            selectorColor = purple,
            inactiveBackgroundColor = Color(0xFF292929),
        )
    } else {
        TimePickerDefaults.colors(
            inactiveBackgroundColor = Color.LightGray,
            activeBackgroundColor = purple.copy(0.1f),
            activeTextColor = purple,
            selectorColor = purple
        )
    }

    val context = LocalContext.current

    DialogAndShowButton(buttonText = "Time Picker Dialog") {
        timepicker(colors = colors) {
            println(it.toString())
            Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
        }

        buttons {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    }

    DialogAndShowButton(buttonText = "Time Picker Dialog With Min/Max") {
        timepicker(
            colors = colors,
            timeRange = LocalTime.of(9, 35)..LocalTime.of(21, 13),
            is24HourClock = false
        ) {
            println(it.toString())
            Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
        }

        buttons {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    }

    DialogAndShowButton(buttonText = "Time Picker Dialog 24H") {
        timepicker(colors = colors, is24HourClock = true) {
            println(it.toString())
            Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
        }

        buttons {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    }

    DialogAndShowButton(buttonText = "Time Picker Dialog 24H With Min/Max") {
        timepicker(
            colors = colors,
            timeRange = LocalTime.of(9, 35)..LocalTime.of(21, 13),
            is24HourClock = true
        ) {
            println(it.toString())
            Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
        }

        buttons {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    }

    DialogAndShowButton(buttonText = "Date Picker Dialog") {
        datepicker {
            println(it.toString())
        }

        buttons {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    }

    DialogAndShowButton(buttonText = "Date and Time Picker Dialog") {
        datetimepicker(timePickerColors = colors) {
            println(it.toString())
        }
    }
}
