package remainder.chronos.data.repoimpl

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import remainder.chronos.data.worker.ReminderWorker
import remainder.chronos.domain.model.Reminder
import remainder.chronos.domain.repository.ScheduleRepository
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context : Context
) : ScheduleRepository {
    override fun scheduleReminder(reminder: Reminder) {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

        val dateTime = "${reminder.reminderDate} ${reminder.reminderTime}"
        val triggerMillis = sdf.parse(dateTime)?.time ?: return
        val delay = triggerMillis - System.currentTimeMillis()
        if (delay <= 0) return
        Log.d("Reminder","Scheduler Impl ${reminder.reminderTitle}")

        val inputData = Data.Builder()
            .putString("title", reminder.reminderTitle)
            .putString("note", reminder.reminderNote ?: "")
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(reminder.reminderId)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}