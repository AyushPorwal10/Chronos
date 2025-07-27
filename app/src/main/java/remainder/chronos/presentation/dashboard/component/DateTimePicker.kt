package remainder.chronos.presentation.dashboard.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DateTimePickers(
    selectedDate: String,
    selectedTime: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("*Date", modifier = Modifier.padding(start = 6.dp, bottom = 4.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDateClick() },
                placeholder = { Text("Pick a date", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text("*Time", modifier = Modifier.padding(start = 6.dp, bottom = 4.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
            OutlinedTextField(
                value = selectedTime,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTimeClick() },
                placeholder = { Text("Pick a time", color = MaterialTheme.colorScheme.onPrimaryContainer) },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    disabledBorderColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}
