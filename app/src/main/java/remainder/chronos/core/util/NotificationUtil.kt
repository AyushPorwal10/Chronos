package remainder.chronos.core.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import remainder.chronos.R

object NotificationUtils {

    fun showReminderNotification(context: Context, title: String, note: String) {
        val channelId = "reminder_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            Log.d("Reminder","Notification Channel created")
        }
        else {
            Log.d("Reminder","Notification Channel Cannnot be created")
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(note)
            .setSmallIcon(R.drawable.app_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        Log.d("Reminder","Notification Built and going to notify")
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
