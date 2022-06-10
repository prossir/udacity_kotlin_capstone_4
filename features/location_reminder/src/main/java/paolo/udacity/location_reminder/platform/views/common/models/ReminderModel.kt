package paolo.udacity.location_reminder.platform.views.common.models

import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.constants.FoundationConstants
import paolo.udacity.foundation.database.models.enums.ReminderStatusEnum


data class ReminderModel(
    var id: Long,
    var title: String,
    var description: String,
    var pointOfInterestId: Long,
    var status: ReminderStatusEnum,
    var createdAt: OffsetDateTime?,
    var updatedAt: OffsetDateTime?,
    var pointOfInterest: PointOfInterestModel
) {

    // Function
    fun reset() {
        id = FoundationConstants.EMPTY_LONG
        title = FoundationConstants.EMPTY_STRING
        description = FoundationConstants.EMPTY_STRING
        status = ReminderStatusEnum.CREATED
        pointOfInterest.reset()
    }

    fun setFromOtherReminder(reminder: ReminderModel) {
        id = reminder.id
        title = reminder.title
        description = reminder.description
        pointOfInterestId = reminder.pointOfInterestId
        status = reminder.status
        createdAt = reminder.createdAt
        updatedAt = reminder.updatedAt
        pointOfInterest.setFromPointOfInterest(reminder.pointOfInterest)
    }

    // Static
    companion object {
        fun new() = ReminderModel(FoundationConstants.EMPTY_LONG,
            FoundationConstants.EMPTY_STRING,
            FoundationConstants.EMPTY_STRING,
            FoundationConstants.EMPTY_LONG,
            ReminderStatusEnum.CREATED,
            null,
            null,
            PointOfInterestModel.new()
        )
    }

}