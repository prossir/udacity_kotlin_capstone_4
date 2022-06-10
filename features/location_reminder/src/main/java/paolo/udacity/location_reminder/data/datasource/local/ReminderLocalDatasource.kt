package paolo.udacity.location_reminder.data.datasource.local

import androidx.lifecycle.LiveData
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.models.ReminderEntity
import paolo.udacity.foundation.database.models.embedded.ReminderWithPointOfInterestEntity
import paolo.udacity.foundation.database.providers.DaoProvider
import paolo.udacity.foundation.providers.DateTimeProvider


class ReminderLocalDatasource(
    daoProvider: DaoProvider,
    private val dateTimeProvider: DateTimeProvider<OffsetDateTime>
) {

    private val reminderDao = daoProvider.getReminderDao()
    private val pointOfInterestDao = daoProvider.getPointOfInterestDao()

    suspend fun maintainReminder(reminderEntity: ReminderEntity) {
        pointOfInterestDao.insertOrUpdate(reminderEntity.pointOfInterest!!, dateTimeProvider).apply {
            reminderEntity.pointOfInterestId = this
            reminderDao.insertOrUpdate(reminderEntity, dateTimeProvider)
        }
    }



    fun retrieveRemindersWithLocation() : LiveData<List<ReminderWithPointOfInterestEntity>> {
        return reminderDao.findByStatusOrderedByLatest()
    }

}