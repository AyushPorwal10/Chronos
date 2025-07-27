package remainder.chronos.presentation.dashboard.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun CustomDialog(title : String , text : String , isLoading: Boolean = false, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(title) },
        text = { Text(text)},
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                if (isLoading)
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else
                    Text("Confirm", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            if(!isLoading){
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }

        }
    )
}
