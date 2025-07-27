package remainder.chronos.presentation.dashboard.state


sealed class AIResponseUiState {
    object Loading : AIResponseUiState()
    object Idle : AIResponseUiState()
    data class ResponseMessage(val message : String) : AIResponseUiState()
    data class ErrorMessage(val message : String) : AIResponseUiState()
}