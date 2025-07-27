package remainder.chronos.presentation.dashboard.state

sealed class ReminderUiState {
    object Loading : ReminderUiState()
    object Idle : ReminderUiState()
    data class SuccessMessage(val message : String) : ReminderUiState()
    data class ErrorMessage(val message : String) : ReminderUiState()
}