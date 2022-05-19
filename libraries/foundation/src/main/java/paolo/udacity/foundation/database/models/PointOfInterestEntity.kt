package paolo.udacity.foundation.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime


@Entity(
    tableName = PointOfInterestEntity.TABLE_NAME,
    indices = [
        Index(value = [PointOfInterestEntity.FIELD_ID])
    ]
)
data class PointOfInterestEntity(
    @ColumnInfo(name = FIELD_ID)
    @PrimaryKey
    var id: Long,
    @ColumnInfo(name = FIELD_LATITUDE)
    var latitude: Double,
    @ColumnInfo(name = FIELD_LONGITUDE)
    var longitude: Double,
    @ColumnInfo(name = FIELD_RANGE_RADIUS)
    var rangeRadius: Double,
    @ColumnInfo(name = FIELD_STATUS)
    var status: Int,
    @ColumnInfo(name = FIELD_CREATED_AT)
    var createdAt: OffsetDateTime?,
    @ColumnInfo(name = FIELD_UPDATED_AT)
    var updatedAt: OffsetDateTime? = createdAt
) {

    companion object {
        internal const val TABLE_NAME = "point_of_interest"

        internal const val FIELD_ID = "poi_id"
        internal const val FIELD_LATITUDE = "latitude"
        internal const val FIELD_LONGITUDE = "longitude"
        internal const val FIELD_RANGE_RADIUS = "range_radius"
        internal const val FIELD_STATUS = "poi_status"
        internal const val FIELD_CREATED_AT = "poi_created_at"
        internal const val FIELD_UPDATED_AT = "poi_updated_at"
    }

}