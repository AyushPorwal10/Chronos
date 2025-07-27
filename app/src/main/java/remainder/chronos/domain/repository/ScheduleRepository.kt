package remainder.chronos.domain.repository

import remainder.chronos.domain.model.Reminder

interface ScheduleRepository {

    fun scheduleReminder(reminder: Reminder)
}