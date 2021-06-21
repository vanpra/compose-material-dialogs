package com.vanpra.composematerialdialogs.color.test.screenshot

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipeLeft
import com.karumi.shot.ScreenshotTest
import com.vanpra.composematerialdialogs.color.ARGBPickerState
import com.vanpra.composematerialdialogs.color.ColorPalette
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.test.utils.DialogWithContent
import com.vanpra.composematerialdialogs.test.utils.extensions.onDialog
import com.vanpra.composematerialdialogs.test.utils.extensions.onDialogColorPicker
import com.vanpra.composematerialdialogs.test.utils.extensions.onDialogColorSelector
import com.vanpra.composematerialdialogs.test.utils.extensions.setContentAndWaitForIdle
import org.junit.Rule
import org.junit.Test

class ColorPickerDialogTests : ScreenshotTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainColorPicker() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                colorChooser(colors = ColorPalette.Primary)
            }
        }
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun mainColorPickerWithInitialSelection() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                colorChooser(colors = ColorPalette.Primary, initialSelection = 4)
            }
        }
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun subColorPicker() {
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                colorChooser(colors = ColorPalette.Primary, subColors = ColorPalette.PrimarySub)
            }
        }

        composeTestRule.onDialogColorSelector(0).performClick()
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun rgbColorPicker() {
        /*  Using list with custom color to ensure the box background uses this color */
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                colorChooser(
                    colors = listOf(Color(100, 100, 200)),
                    argbPickerState = ARGBPickerState.WithoutAlphaSelector,
                    subColors = ColorPalette.PrimarySub
                )
            }
        }

        composeTestRule.onDialogColorPicker().performGesture { swipeLeft() }
        composeTestRule.waitForIdle()
        compareScreenshot(composeTestRule.onDialog())
    }

    @Test
    fun argbColorPicker() {
        /* Using color with 0 alpha to test squares background */
        composeTestRule.setContentAndWaitForIdle {
            DialogWithContent {
                colorChooser(
                    colors = listOf(Color(0, 0, 0, 0)),
                    argbPickerState = ARGBPickerState.WithAlphaSelector,
                    subColors = ColorPalette.PrimarySub
                )
            }
        }

        composeTestRule.onDialogColorPicker().performGesture { swipeLeft() }
        composeTestRule.waitForIdle()
        compareScreenshot(composeTestRule.onDialog())
    }
}
