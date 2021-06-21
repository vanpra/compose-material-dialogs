package com.vanpra.composematerialdialogs.test.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.buttons

@Composable
fun DialogWithContent(
    autoDismiss: Boolean = true,
    dialog: MaterialDialog = MaterialDialog(autoDismiss),
    content: @Composable MaterialDialog.() -> Unit
) {
    MaterialTheme {
        Box(Modifier.fillMaxSize()) {
            dialog.build { content() }
            SideEffect { dialog.show() }
        }
    }
}

@Composable
fun MaterialDialog.defaultButtons() {
    buttons {
        negativeButton("Cancel")
        positiveButton("Ok")
    }
}

fun <T> Collection<T>.powerSet(): Set<Set<T>> = when {
    isEmpty() -> setOf(setOf())
    else -> drop(1).powerSet().let { it + it.map { rest -> rest + first() } }
}
