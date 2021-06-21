package com.vanpra.composematerialdialogs.datetime.date

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

/**
 * Represents the colors used by a [timepicker] and its parts in different states
 *
 * See [DatePickerDefaults.colors] for the default implementation
 */
interface DatePickerColors {
    val headerBackgroundColor: Color
    val headerTextColor: Color

    /**
     * Gets the background color dependant on if the item is active or not
     *
     * @param active true if the component/item is selected and false otherwise
     * @return background color as a State
     */
    @Composable
    fun backgroundColor(active: Boolean): State<Color>

    /**
     * Gets the text color dependant on if the item is active or not
     *
     * @param active true if the component/item is selected and false otherwise
     * @return text color as a State
     */
    @Composable
    fun textColor(active: Boolean): State<Color>
}

internal class DefaultDatePickerColors(
    override val headerBackgroundColor: Color,
    override val headerTextColor: Color,
    private val activeBackgroundColor: Color,
    private val inactiveBackgroundColor: Color,
    private val activeTextColor: Color,
    private val inactiveTextColor: Color
) : DatePickerColors {
    @Composable
    override fun backgroundColor(active: Boolean): State<Color> {
        return rememberUpdatedState(if (active) activeBackgroundColor else inactiveBackgroundColor)
    }

    @Composable
    override fun textColor(active: Boolean): State<Color> {
        return rememberUpdatedState(if (active) activeTextColor else inactiveTextColor)
    }
}
