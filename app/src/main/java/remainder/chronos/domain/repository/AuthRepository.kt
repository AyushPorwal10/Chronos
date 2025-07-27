package remainder.chronos.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signInWithGoogle(idToken: String): Flow<Result<FirebaseUser?>>

    fun isUserLoggedIn() : Boolean
}