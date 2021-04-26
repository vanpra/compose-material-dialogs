package com.vanpra.composematerialdialogdemos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog

/**
 * @brief Builds a dialog and adds button to the layout which shows the dialog on click
 */
@Composable
fun DialogAndShowButton(buttonText: String, content: @Composable MaterialDialog.() -> Unit) {
    val dialog = remember { MaterialDialog() }

    dialog.build(content = content)

    TextButton(
        onClick = { dialog.show() },
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp).background(MaterialTheme.colors.primaryVariant),
    ) {
        Text(
            buttonText,
            modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

/**
 * @brief Add title to top of layout
 */
@Composable
fun DialogSection(title: String, content: @Composable () -> Unit) {
    Text(
        title,
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
    )

    content()
}
