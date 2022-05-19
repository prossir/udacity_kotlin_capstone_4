package paolo.udacity.auth.domain.entity

import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.models.enums.UserStatusEnum


data class User(
    val id: Long,
    val fullName: String,
    val email: String,
    val status : UserStatusEnum,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime?
)