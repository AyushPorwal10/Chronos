package remainder.chronos.data.repoimpl


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import remainder.chronos.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override  fun signInWithGoogle(idToken: String): Flow<Result<FirebaseUser?>> = flow {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            emit(Result.success(result.user))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun isUserLoggedIn(): Boolean {
      return firebaseAuth.currentUser != null
    }


}