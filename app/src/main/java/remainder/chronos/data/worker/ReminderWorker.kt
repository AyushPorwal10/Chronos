package remainder.chronos.data.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import remainder.chronos.core.util.NotificationUtils


class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val title = inputData.getString("title") ?: return Result.failure()
        val note = inputData.getString("note") ?: ""
        Log.d("Reminder","Going to show Reminder")
        NotificationUtils.showReminderNotification(applicationContext, title, note)
        return Result.success()
    }
}
