package remainder.chronos.presentation.dashboard.state

import remainder.chronos.domain.model.Reminder

sealed class FetchReminderUiState {
    object Loading : FetchReminderUiState()
    object Idle : FetchReminderUiState()
    data class Success(val reminders : List<Reminder>) : FetchReminderUiState()
    data class ErrorMessage(val message : String) : FetchReminderUiState()
}