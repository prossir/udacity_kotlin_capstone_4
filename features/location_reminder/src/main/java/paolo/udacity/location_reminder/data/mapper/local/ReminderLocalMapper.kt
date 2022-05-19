package paolo.udacity.location_reminder.data.mapper.local

import paolo.udacity.foundation.database.models.ReminderEntity
import paolo.udacity.foundation.mappers.Mapper
import paolo.udacity.location_reminder.domain.entity.Reminder


class ReminderLocalMapper(
    private val pointOfInterestLocalMapper: PointOfInterestLocalMapper
    ): Mapper<ReminderEntity, Reminder>() {

    override fun reverseMap(value: Reminder): ReminderEntity {
        ReminderEntity(
            id = value.id,
            title = value.title,
            description = value.description,
            pointOfInterestId = value.pointOfInterestId,
            status = value.status,
            createdAt = value.createdAt,
            updatedAt = value.updatedAt
        ).apply {
            this.pointOfInterest = pointOfInterestLocalMapper.reverseMap(value.pointOfInterest)
            return this
        }
    }

    override fun map(value: ReminderEntity) = Reminder(
        id = value.id,
        title = value.title,
        description = value.description,
        pointOfInterestId = value.pointOfInterestId,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt,
        pointOfInterest = pointOfInterestLocalMapper.map(value.pointOfInterest!!)
    )

}