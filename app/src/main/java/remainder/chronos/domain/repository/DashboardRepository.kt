package remainder.chronos.domain.repository

import kotlinx.coroutines.flow.Flow
import remainder.chronos.domain.model.Reminder
import remainder.chronos.presentation.dashboard.state.ReminderUiState
import remainder.chronos.presentation.dashboard.state.FetchReminderUiState

interface DashboardRepository {


    suspend fun uploadReminderImage(imageUri: String?, reminderId: String, userId: String): String?

    suspend fun saveReminder(
        userId: String,
        reminder: Reminder,
        onStateChange: (ReminderUiState) -> Unit
    )

    suspend fun fetchReminder(userId : String ) : Flow<FetchReminderUiState>

    suspend fun deleteReminder(userId: String , reminderId: String,   onStateChange: (ReminderUiState) -> Unit)

    suspend fun updateReminder(userId: String , reminder: Reminder,   onStateChange: (ReminderUiState) -> Unit)


    suspend fun logout()
}