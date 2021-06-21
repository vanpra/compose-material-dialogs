package com.vanpra.composematerialdialogs.datetime.date

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Object to hold default values used by [datepicker]
 */
object DatePickerDefaults {
    /**
     * Initialises a [DatePickerColors] object which represents the different colors used by
     * the [datepicker] composable
     */
    @Composable
    fun colors(
        headerBackgroundColor: Color = MaterialTheme.colors.primary,
        headerTextColor: Color = MaterialTheme.colors.onPrimary,
        activeBackgroundColor: Color = MaterialTheme.colors.primary,
        inactiveBackgroundColor: Color = Color.Transparent,
        activeTextColor: Color = MaterialTheme.colors.onPrimary,
        inactiveTextColor: Color = MaterialTheme.colors.onBackground
    ): DatePickerColors {
        return DefaultDatePickerColors(
            headerBackgroundColor = headerBackgroundColor,
            headerTextColor = headerTextColor,
            activeBackgroundColor = activeBackgroundColor,
            inactiveBackgroundColor = inactiveBackgroundColor,
            activeTextColor = activeTextColor,
            inactiveTextColor = inactiveTextColor,
        )
    }
}
