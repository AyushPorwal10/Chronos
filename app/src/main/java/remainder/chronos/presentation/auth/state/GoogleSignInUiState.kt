package remainder.chronos.presentation.auth.state

import com.google.firebase.auth.FirebaseUser


sealed class GoogleSignInUiState {
    object Idle : GoogleSignInUiState()
    object Loading : GoogleSignInUiState()
    data class Success(val user: FirebaseUser) : GoogleSignInUiState()
    data class Error(val message: String) : GoogleSignInUiState()
}
