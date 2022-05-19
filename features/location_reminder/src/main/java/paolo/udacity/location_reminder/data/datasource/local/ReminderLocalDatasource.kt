package paolo.udacity.location_reminder.data.datasource.local

import androidx.lifecycle.LiveData
import paolo.udacity.foundation.database.models.ReminderEntity
import paolo.udacity.foundation.database.models.embedded.ReminderWithPointOfInterestEntity
import paolo.udacity.foundation.database.providers.DaoProvider


class ReminderLocalDatasource(
    daoProvider: DaoProvider
) {

    private val reminderDao = daoProvider.getReminderDao()

    suspend fun maintainReminder(reminderEntity: ReminderEntity) {
        reminderDao.insertOrUpdate(reminderEntity)
    }

    fun retrieveRemindersWithLocation() : LiveData<List<ReminderWithPointOfInterestEntity>> {
        return reminderDao.findByStatusOrderedByLatest()
    }

}