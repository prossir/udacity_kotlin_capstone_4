package paolo.udacity.location_reminder.data.mapper.local

import paolo.udacity.foundation.database.models.ReminderEntity
import paolo.udacity.foundation.database.models.embedded.ReminderWithPointOfInterestEntity
import paolo.udacity.foundation.mappers.SingleMapper


class ReminderWithPointOfInterestLocalMapper: SingleMapper<ReminderWithPointOfInterestEntity, ReminderEntity>() {

    override fun map(value: ReminderWithPointOfInterestEntity) = ReminderEntity(
        id = value.reminder.id,
        title = value.reminder.title,
        description = value.reminder.description,
        pointOfInterestId = value.reminder.pointOfInterestId,
        status = value.reminder.status,
        createdAt = value.reminder.createdAt,
        updatedAt = value.reminder.updatedAt,
    ).apply {
        this.pointOfInterest = value.pointOfInterest
    }

}