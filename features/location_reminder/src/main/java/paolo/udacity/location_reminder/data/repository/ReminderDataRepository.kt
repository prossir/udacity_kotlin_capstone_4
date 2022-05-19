package paolo.udacity.location_reminder.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import paolo.udacity.location_reminder.data.datasource.local.ReminderLocalDatasource
import paolo.udacity.location_reminder.data.mapper.local.ReminderLocalMapper
import paolo.udacity.location_reminder.data.mapper.local.ReminderWithPointOfInterestLocalMapper
import paolo.udacity.location_reminder.domain.entity.Reminder
import paolo.udacity.location_reminder.domain.repository.ReminderRepository


class ReminderDataRepository(
    private val reminderLocalDatasource: ReminderLocalDatasource,
    private val reminderLocalMapper: ReminderLocalMapper,
    private val structuredReminderLocalMapper: ReminderWithPointOfInterestLocalMapper
): ReminderRepository {

    override suspend fun maintainReminder(reminder: Reminder) {
        reminderLocalMapper.reverseMap(reminder).apply {
            reminderLocalDatasource.maintainReminder(this)
        }
    }

    override suspend fun findCurrentRemindersByLatest(): LiveData<List<Reminder>> {
        return Transformations.map(reminderLocalDatasource.retrieveRemindersWithLocation()) {
            reminderLocalMapper.map(structuredReminderLocalMapper.map(it))
        }
    }

}