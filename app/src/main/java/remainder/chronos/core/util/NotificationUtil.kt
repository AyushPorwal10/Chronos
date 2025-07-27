package remainder.chronos.core.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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


    @Composable
    fun HandleNotificationPermission(

    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val context = LocalContext.current
        var askAgain by remember { mutableStateOf(false) }
        var askedAlready by remember { mutableStateOf(false) }

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // User allowed notifications
            } else {
                UiMessage.showToast(context , context.getString(R.string.notification_cannot_be_sent))
            }
        }

        LaunchedEffect(Unit) {
            if (!askedAlready) {
                askedAlready = true
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        //User allowed notification
                    }

                    askAgainToUser(context as? ComponentActivity, Manifest.permission.POST_NOTIFICATIONS) -> {
                        askAgain = true
                    }

                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }
        }

        if (askAgain) {
            AskAgainDialog(
                onConfirm = {
                    askAgain = false
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                },
                onDismiss = {
                    askAgain = false
                }
            )
        }
    }

    private fun askAgainToUser(activity: androidx.activity.ComponentActivity?, permission: String): Boolean {
        return activity?.shouldShowRequestPermissionRationale(permission) == true
    }



    @Composable
    fun AskAgainDialog(
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Notification Permission Needed") },
            text = { Text(stringResource(R.string.why_we_ask_notification) )},
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No Thanks")
                }
            }
        )
    }
}
