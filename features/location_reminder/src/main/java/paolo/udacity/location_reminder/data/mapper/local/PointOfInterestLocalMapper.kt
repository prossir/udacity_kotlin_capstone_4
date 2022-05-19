package paolo.udacity.location_reminder.data.mapper.local

import paolo.udacity.foundation.database.models.PointOfInterestEntity
import paolo.udacity.foundation.mappers.Mapper
import paolo.udacity.location_reminder.domain.entity.PointOfInterest


class PointOfInterestLocalMapper: Mapper<PointOfInterestEntity, PointOfInterest>() {

    override fun reverseMap(value: PointOfInterest) = PointOfInterestEntity(
        id = value.id,
        latitude = value.latitude,
        longitude = value.longitude,
        rangeRadius = value.rangeRadius,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt
    )

    override fun map(value: PointOfInterestEntity) = PointOfInterest(
        id = value.id,
        latitude = value.latitude,
        longitude = value.longitude,
        rangeRadius = value.rangeRadius,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt
    )

}