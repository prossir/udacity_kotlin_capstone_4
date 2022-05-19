package paolo.udacity.location_reminder.domain.entity

import org.threeten.bp.OffsetDateTime


class PointOfInterest(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val rangeRadius: Double,
    val status: Int,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?
)