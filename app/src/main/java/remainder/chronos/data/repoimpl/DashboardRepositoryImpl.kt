package remainder.chronos.data.repoimpl

import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import remainder.chronos.domain.model.Reminder
import remainder.chronos.domain.repository.DashboardRepository
import remainder.chronos.presentation.dashboard.state.FetchReminderUiState
import remainder.chronos.presentation.dashboard.state.ReminderUiState
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : DashboardRepository {

    val TAG = "DashboardRepositoryImpl"

    override suspend fun uploadReminderImage(
        imageUri: String?,
        reminderId: String,
        userId: String
    ): String? {

        return try {


            if (imageUri == null)
                return null
            val reference =
                firebaseStorage.reference.child("chronos").child(userId).child(reminderId)


            reference.putFile(imageUri.toUri()).await()
            val downloadUri = reference.downloadUrl.await()
            downloadUri.toString()

        } catch (exception: Exception) {
            null
        }


    }

    override suspend fun saveReminder(
        userId: String,
        reminder: Reminder,
        onStateChange: (ReminderUiState) -> Unit
    ) {

        onStateChange(ReminderUiState.Loading)
        try {

            // Generating a unique reminder id
            val documentReference = firebaseFireStore.collection("chronos").document(userId).collection("reminders").document()

            reminder.reminderImage = uploadReminderImage(reminder.reminderImage, documentReference.id, userId)

            reminder.reminderId = documentReference.id

            documentReference.set(reminder).await()
            onStateChange(ReminderUiState.SuccessMessage("Reminder Added Successfully"))
        } catch (exception: Exception) {
            Log.e(TAG, "Error in saving reminder : ", exception)
            onStateChange(
                ReminderUiState.ErrorMessage(
                    exception.localizedMessage ?: "Something went wrong\nPlease try again later"
                )
            )
        }
    }

    override suspend fun fetchReminder(userId: String): Flow<FetchReminderUiState> = callbackFlow {

        val listenerRegistration = try {
            trySend(FetchReminderUiState.Loading)

            val reminderCollection = firebaseFireStore
                .collection("chronos")
                .document(userId)
                .collection("reminders")

            reminderCollection.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    trySend(
                        FetchReminderUiState.ErrorMessage(
                            exception.localizedMessage ?: "Something went wrong"
                        )
                    )
                    return@addSnapshotListener
                }

                val reminders = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Reminder::class.java)?.copy(reminderId = doc.id)
                }?.sortedBy { it.reminderDate } ?: emptyList()

                trySend(FetchReminderUiState.Success(reminders))
            }
        } catch (e: Exception) {
            trySend(
                FetchReminderUiState.ErrorMessage(
                    e.localizedMessage ?: "Failed to fetch reminders"
                )
            )
            null
        }

        awaitClose {
            listenerRegistration?.remove()
        }
    }

    override suspend fun deleteReminder(
        userId: String,
        reminderId: String,
        onStateChange: (ReminderUiState) -> Unit
    ) {
        try {
            Log.d("Reminder", "Reminder id is $reminderId")
            onStateChange(ReminderUiState.Loading)

            firebaseFireStore.collection("chronos").document(userId).collection("reminders")
                .document(reminderId).delete().await()
            onStateChange(ReminderUiState.SuccessMessage("Deleted Successfully"))
        } catch (exception: Exception) {
            onStateChange(
                ReminderUiState.ErrorMessage(
                    exception.localizedMessage ?: "Unknown Error"
                )
            )
        }
    }

    override suspend fun updateReminder(
        userId: String,
        reminder: Reminder,
        onStateChange: (ReminderUiState) -> Unit
    ) {
        try {
            onStateChange(ReminderUiState.Loading)

            firebaseFireStore.collection("chronos").document(userId).collection("reminders")
                .document(reminder.reminderId).set(reminder).await()
            onStateChange(ReminderUiState.SuccessMessage("Updated Successfully"))
        } catch (exception: Exception) {
            onStateChange(
                ReminderUiState.ErrorMessage(
                    exception.localizedMessage ?: "Unknown Error"
                )

            )
        }
    }

    override suspend fun deleteReminderImage(userId: String, reminderId: String) {
        try {
            firebaseStorage.reference.child("chronos").child(userId).child(reminderId).delete().await()
        }
        catch (exception : Exception){

        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }
}
