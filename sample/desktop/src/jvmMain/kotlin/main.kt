import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.Dialog

fun main() = Window {
    val showingDialog = mutableStateOf(false)

    MaterialTheme {
        Button(onClick = {
            showingDialog.value = true
        }) {
            Text("Show dialog")
        }

        if (showingDialog.value) {
            Dialog(onDismissRequest = {}) {
                Box {
                    Text("Hello")
                }
            }
        }
    }
}