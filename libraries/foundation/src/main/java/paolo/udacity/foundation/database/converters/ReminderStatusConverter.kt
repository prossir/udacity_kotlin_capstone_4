package paolo.udacity.foundation.database.converters

import androidx.room.TypeConverter
import paolo.udacity.foundation.database.models.enums.ReminderStatusEnum


class ReminderStatusConverter {

    @TypeConverter
    fun fromPointOfInterestStatus(pointOfInterestStatus: ReminderStatusEnum): String {
        return pointOfInterestStatus.name
    }

    @TypeConverter
    fun toPointOfInterestStatus(pointOfInterestStatus: String): ReminderStatusEnum {
        return ReminderStatusEnum.valueOf(pointOfInterestStatus)
    }

}