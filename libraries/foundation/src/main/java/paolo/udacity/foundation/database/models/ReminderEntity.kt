package paolo.udacity.foundation.database.models

import androidx.room.*
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.models.enums.ReminderStatusEnum


@Entity(
    tableName = ReminderEntity.TABLE_NAME,
    indices = [
        Index(value = [ReminderEntity.FIELD_ID])
    ]
)
data class ReminderEntity(
    @ColumnInfo(name = FIELD_ID)
    @PrimaryKey
    var id: Long,
    @ColumnInfo(name = FIELD_TITLE)
    var title: String,
    @ColumnInfo(name = FIELD_DESCRIPTION)
    var description: String,
    @ColumnInfo(name = FIELD_LOCATION_ID)
    var pointOfInterestId: Long,
    @ColumnInfo(name = FIELD_STATUS)
    val status : ReminderStatusEnum = ReminderStatusEnum.CREATED,
    @ColumnInfo(name = FIELD_CREATED_AT)
    var createdAt: OffsetDateTime?,
    @ColumnInfo(name = FIELD_UPDATED_AT)
    var updatedAt: OffsetDateTime? = createdAt
) {

    @Ignore
    var pointOfInterest: PointOfInterestEntity? = null

    companion object {
        internal const val TABLE_NAME = "reminder"

        internal const val FIELD_ID = "id"
        internal const val FIELD_TITLE = "title"
        internal const val FIELD_DESCRIPTION = "description"
        internal const val FIELD_LOCATION_ID = "point_of_interest_id"
        internal const val FIELD_STATUS = "status"
        internal const val FIELD_CREATED_AT = "created_at"
        internal const val FIELD_UPDATED_AT = "updated_at"
    }

}