package paolo.udacity.location_reminder.domain.repository

import androidx.lifecycle.LiveData
import paolo.udacity.location_reminder.domain.entity.Reminder


interface ReminderRepository {

    suspend fun maintainReminder(reminder: Reminder)
    suspend fun findCurrentRemindersByLatest(): LiveData<List<Reminder>>

}