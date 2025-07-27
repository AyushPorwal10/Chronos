package remainder.chronos.presentation.dashboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun OutlinedInputField(
    label: String,
    value: String,
    icon: Int,
    onValueChange: (String) -> Unit,
    height: Dp = Dp.Unspecified
) {
    Column(modifier = Modifier.fillMaxWidth(0.9f)) {
        Text(
            label,
            modifier = Modifier.padding(start = 6.dp, bottom = 4.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            },
            placeholder = { Text(label, color = MaterialTheme.colorScheme.onPrimaryContainer) },
            modifier = Modifier
                .fillMaxWidth()
                .then(if (height != Dp.Unspecified) Modifier.height(height) else Modifier),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        )
    }
}
