package remainder.chronos.presentation.dashboard.component

import android.content.Context
import remainder.chronos.R
import remainder.chronos.core.util.DateAndTimeUtil
import remainder.chronos.core.util.UiMessage


object ValidateInput {
    fun validateReminderInput(
        title: String,
        selectedDate: String,
        selectedTime: String,
        context: Context
    ) : Boolean{

        return when{
            DateAndTimeUtil.isTimeInPast(selectedTime, selectedDate) -> {
                UiMessage.showToast(context, context.getString(R.string.enter_valid_date))
                false
            }

            title.trim().isEmpty() -> {
                UiMessage.showToast(context, context.getString(R.string.enter_remindere_title))
                false
            }

            else -> true
        }
    }
}