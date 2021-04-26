package com.vanpra.common
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*

@Composable
expect fun Dialog()


@Composable
fun App() {
    var showingDialog by remember { mutableStateOf(false) }

    MaterialTheme {
        Button(onClick = {
            showingDialog = true
        }) {
            Text("Show dialog")
        }
    }
}

