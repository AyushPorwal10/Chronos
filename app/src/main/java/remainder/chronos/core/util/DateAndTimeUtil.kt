package remainder.chronos.core.util

import java.text.SimpleDateFormat
import java.util.Locale

object DateAndTimeUtil {

    fun isTimeInPast(reminderTime : String , reminderDate : String) : Boolean {
        if (reminderDate.isBlank() || reminderTime.isBlank()) return true
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
        val dateTime = "$reminderDate $reminderTime"

        val triggerMillis = sdf.parse(dateTime)?.time ?: return true
        val delay = triggerMillis - System.currentTimeMillis()

        return delay <= 0
    }
}