package paolo.udacity.foundation.database.models.embedded

import androidx.room.Embedded
import androidx.room.Relation
import paolo.udacity.foundation.database.models.PointOfInterestEntity
import paolo.udacity.foundation.database.models.ReminderEntity


data class ReminderWithPointOfInterestEntity(
    @Embedded
    val reminder: ReminderEntity,

    @Relation(
        parentColumn = POINT_OF_INTEREST_ID_IDENTIFIER,
        entityColumn = POINT_OF_INTEREST_ID_IN_REMINDER
    )
    val pointOfInterest: PointOfInterestEntity
) {

    companion object {
        internal const val POINT_OF_INTEREST_ID_IDENTIFIER = "point_of_interest_id"
        internal const val POINT_OF_INTEREST_ID_IN_REMINDER = "poi_id"
    }

}