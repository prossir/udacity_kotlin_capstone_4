package paolo.udacity.foundation.database.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.models.PointOfInterestEntity
import paolo.udacity.foundation.providers.DateTimeProvider


@Dao
abstract class PointOfInterestDao: BaseDao<PointOfInterestEntity> {

    @Transaction
    open suspend fun insertOrUpdate(entity: PointOfInterestEntity,
                                    dateTimeProvider: DateTimeProvider<OffsetDateTime>): Long {
        val now = dateTimeProvider.now()
        val item = findById(entity.id)
        if (item == null) {
            insert(entity.apply {
                createdAt = now
                updatedAt = now
            }).apply {
                entity.id = this
            }
        } else {
            update(entity.apply {
                createdAt = item.createdAt
                updatedAt = now
            })
        }
        return entity.id
    }

    @Query("SELECT * FROM point_of_interest WHERE poi_id = :id")
    abstract suspend fun findById(id: Long): PointOfInterestEntity?

    @Query("DELETE FROM point_of_interest")
    abstract suspend fun cleanTable()

}