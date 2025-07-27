package remainder.chronos.presentation.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import remainder.chronos.domain.repository.AuthRepository
import remainder.chronos.presentation.auth.state.GoogleSignInUiState
import javax.inject.Inject


@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(){

    val TAG = "AUTH_VIEWMODEL"
    private val _googleSignInUiState = MutableStateFlow<GoogleSignInUiState>(GoogleSignInUiState.Idle)
    val googleSignInUiState: StateFlow<GoogleSignInUiState> = _googleSignInUiState.asStateFlow()





    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _googleSignInUiState.value = GoogleSignInUiState.Loading
            authRepository.signInWithGoogle(idToken)
                .catch { e -> _googleSignInUiState.value = GoogleSignInUiState.Error(e.message ?: "Unknown error") }
                .collect { result ->
                    result.onSuccess { user ->
                        if (user != null)
                            _googleSignInUiState.value = GoogleSignInUiState.Success(user)
                        else
                            _googleSignInUiState.value = GoogleSignInUiState.Error("User is null")
                    }
                    result.onFailure {
                        _googleSignInUiState.value = GoogleSignInUiState.Error(it.message ?: "Unknown failure")
                    }
                }
        }
    }

    fun resetGoogleSignInState() {
        _googleSignInUiState.value = GoogleSignInUiState.Idle
    }

    fun isLoggedIn() : Boolean{
        return authRepository.isUserLoggedIn()
    }

}