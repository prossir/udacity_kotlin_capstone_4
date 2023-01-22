package paolo.udacity.location_reminder.features.views.common.mappers

import paolo.udacity.foundation.mappers.Mapper
import paolo.udacity.location_reminder.domain.entity.PointOfInterest
import paolo.udacity.location_reminder.features.views.common.models.PointOfInterestModel


class PointOfInterestMapper: Mapper<PointOfInterest, PointOfInterestModel>() {

    override fun reverseMap(value: PointOfInterestModel) = PointOfInterest(
        id = value.id,
        latitude = value.latitude,
        longitude = value.longitude,
        rangeRadius = value.rangeRadius,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt
    )

    override fun map(value: PointOfInterest) = PointOfInterestModel(
        id = value.id,
        latitude = value.latitude,
        longitude = value.longitude,
        rangeRadius = value.rangeRadius,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt
    )

}