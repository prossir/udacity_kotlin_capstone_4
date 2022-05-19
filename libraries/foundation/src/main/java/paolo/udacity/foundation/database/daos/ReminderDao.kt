package paolo.udacity.foundation.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.models.ReminderEntity
import paolo.udacity.foundation.database.models.embedded.ReminderWithPointOfInterestEntity
import paolo.udacity.foundation.database.models.enums.ReminderStatusEnum
import paolo.udacity.foundation.providers.DateTimeProvider
import javax.inject.Inject


@Dao
abstract class ReminderDao: BaseDao<ReminderEntity> {

    @Inject
    lateinit var dateTimeProvider: DateTimeProvider<OffsetDateTime>

    @Transaction
    open suspend fun insertOrUpdate(entity: ReminderEntity) {
        val now = dateTimeProvider.now()
        val item = findById(entity.id)
        if (item == null) {
            insert(entity.apply {
                createdAt = now
                updatedAt = now
            })
        } else {
            update(entity.apply {
                createdAt = item.createdAt
                updatedAt = now
            })
        }
    }

    @Query("SELECT * FROM reminder WHERE id = :id")
    abstract suspend fun findById(id: Long): ReminderEntity?

    @Transaction
    @Query("SELECT * FROM reminder WHERE status = :currentStatus ORDER BY updated_at")
    abstract fun findByStatusOrderedByLatest(
        currentStatus: ReminderStatusEnum = ReminderStatusEnum.CREATED
    ): LiveData<List<ReminderWithPointOfInterestEntity>>

    @Query("DELETE FROM reminder")
    abstract suspend fun cleanTable()


}