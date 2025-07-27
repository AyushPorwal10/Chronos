package remainder.chronos.domain.model

import android.net.Uri

data class Reminder(
    var reminderId : String = "",
    val reminderTitle: String = "",
    val reminderDate: String = "",
    val reminderTime: String = "",
    var reminderImage: String? = null,
    val reminderNote: String?= null
)
