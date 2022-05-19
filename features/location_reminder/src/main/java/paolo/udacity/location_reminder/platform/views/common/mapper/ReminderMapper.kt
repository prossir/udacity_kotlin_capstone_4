package paolo.udacity.location_reminder.platform.views.common.mapper

import paolo.udacity.foundation.mappers.Mapper
import paolo.udacity.location_reminder.domain.entity.Reminder
import paolo.udacity.location_reminder.platform.views.common.models.ReminderModel


class ReminderMapper(
    private val pointOfInterestMapper: PointOfInterestMapper
    ): Mapper<Reminder, ReminderModel>() {

    override fun reverseMap(value: ReminderModel) = Reminder(
        id = value.id,
        title = value.title,
        description = value.description,
        pointOfInterestId = value.pointOfInterestId,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt,
        pointOfInterest = pointOfInterestMapper.reverseMap(value.pointOfInterest)
    )

    override fun map(value: Reminder) = ReminderModel(
        id = value.id,
        title = value.title,
        description = value.description,
        pointOfInterestId = value.pointOfInterestId,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt,
        pointOfInterest = pointOfInterestMapper.map(value.pointOfInterest)
    )

}