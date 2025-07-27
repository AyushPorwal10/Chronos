package remainder.chronos.presentation.dashboard.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import remainder.chronos.R

@Composable
fun DeleteReminderDialog(
    visible: Boolean,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (visible) {
        CustomDialog(
            title = stringResource(R.string.delete_reminder),
            text = stringResource(R.string.sure_to_delele),
            isLoading = isLoading,
            onDismiss = onDismiss,
            onConfirm = onConfirm
        )
    }
}
