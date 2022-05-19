package paolo.udacity.location_reminder.domain.entity

import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.models.enums.ReminderStatusEnum


data class Reminder(
    val id: Long,
    var title: String,
    var description: String,
    var pointOfInterestId: Long,
    val status: ReminderStatusEnum,
    var createdAt: OffsetDateTime?,
    var updatedAt: OffsetDateTime?,
    var pointOfInterest: PointOfInterest
)