package remainder.chronos.presentation.dashboard.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import remainder.chronos.core.util.NotificationUtils.HandleNotificationPermission
import remainder.chronos.domain.repository.DashboardRepository
import remainder.chronos.domain.model.Reminder
import remainder.chronos.domain.repository.AiMessageRepository
import remainder.chronos.domain.repository.ScheduleRepository
import remainder.chronos.presentation.dashboard.state.ReminderUiState
import remainder.chronos.presentation.dashboard.state.FetchReminderUiState
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val dashboardRepository: DashboardRepository,
    private val scheduleRepository: ScheduleRepository,
    private val aiMessageRepository: AiMessageRepository
)  : ViewModel(){

    private  var userId : String?


    val TAG = "DashboardViewModel"

    private val _addUpdateReminderUiState = MutableStateFlow<ReminderUiState>(ReminderUiState.Idle)
    val addUpdateReminderUiState : StateFlow<ReminderUiState> = _addUpdateReminderUiState.asStateFlow()

    private val _fetchReminderUiState = MutableStateFlow<FetchReminderUiState>(FetchReminderUiState.Idle)
    val fetchReminderUiState : StateFlow<FetchReminderUiState> = _fetchReminderUiState.asStateFlow()

    private val _deleteReminderUiState = MutableStateFlow<ReminderUiState>(ReminderUiState.Idle)
    val deleteReminderUiState : StateFlow<ReminderUiState> = _deleteReminderUiState.asStateFlow()


    private val _aiResponseUiState = MutableStateFlow<ReminderUiState>(ReminderUiState.Idle)
    val aiResponseUiState : StateFlow<ReminderUiState> = _aiResponseUiState.asStateFlow()



    private val _selectedReminder = mutableStateOf<Reminder?>(null)
    val selectedReminder: State<Reminder?> get() = _selectedReminder

    fun setSelectedReminder(remainder: Reminder) {
        _selectedReminder.value = remainder
    }

    fun resetSelectedReminder() {
        _selectedReminder.value = null
    }

    fun resetAddUpdateReminderUiState(){
        _addUpdateReminderUiState.value = ReminderUiState.Idle
    }
    fun resetAiMessageUiState(){
        _aiResponseUiState.value = ReminderUiState.Idle
    }




    init {
        userId = firebaseAuth.currentUser?.uid
        fetchReminders()
    }

    fun addReminder(reminder : Reminder) {
        viewModelScope.launch {
            userId?.let {

                    dashboardRepository.saveReminder(userId = it, reminder) { uiState ->
                        Log.d(TAG, "Reminder saved")
                        _addUpdateReminderUiState.value = uiState
                        scheduleRepository.scheduleReminder(reminder)
                    }
            }
        }
    }


    private fun fetchReminders(){
        viewModelScope.launch {
            userId?.let {
                  dashboardRepository.fetchReminder(it).collect{remindersState ->
                      _fetchReminderUiState.value = remindersState
                }
            }
        }
    }
    fun deleteReminder(reminderId : String ){
        viewModelScope.launch {
            userId?.let {

                val deleteReminder  = launch {
                    dashboardRepository.deleteReminder(it , reminderId  , onStateChange = {
                        _deleteReminderUiState.value = it
                    })
                }

                val deleteReminderImage = launch {
                    dashboardRepository.deleteReminderImage(userId = it , reminderId)
                }

                deleteReminder.join()
                deleteReminderImage.join()

                // Cancelling scheduled reminder
                scheduleRepository.cancelReminder(reminderId)

            }
        }
    }

    fun updateReminder(reminder: Reminder){
        viewModelScope.launch {
            userId?.let {
                dashboardRepository.updateReminder(it ,reminder ,  onStateChange = {uistate->
                    _addUpdateReminderUiState.value = uistate
                })
            }
        }
    }

    fun resetDeleteReminderState(){
        _deleteReminderUiState.value = ReminderUiState.Idle
    }

    fun logout(){
        viewModelScope.launch {
            dashboardRepository.logout()
        }
    }



     fun createAiMessage(prompt : String ){
        viewModelScope.launch {



            _aiResponseUiState.value = ReminderUiState.Loading
            val aiPrompt = "$prompt Give me a concise response, no extra words."
            val encodedPrompt = URLEncoder.encode(aiPrompt, StandardCharsets.UTF_8.toString())

            val response = aiMessageRepository.getAiMotivationalMessage(encodedPrompt)
            if(response.isSuccessful){
                val body = response.body()
                if(body != null){
                    val bodyString = withContext(Dispatchers.IO){
                        body.string()
                    }
                    _aiResponseUiState.value = ReminderUiState.SuccessMessage(bodyString)
                }

            }
            else {
               _aiResponseUiState.value = ReminderUiState.ErrorMessage(response.body()?.string() ?: "Something went wrong")
            }
        }
    }
}