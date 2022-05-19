package paolo.udacity.auth.platform.views.common.model

import com.google.firebase.auth.FirebaseUser
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.constants.FoundationConstants
import paolo.udacity.foundation.database.models.enums.UserStatusEnum


data class UserModel(
    val id: Long,
    val fullName: String,
    val email: String,
    val status : UserStatusEnum,
    val createdAt: OffsetDateTime?,
    val updatedAt: OffsetDateTime? = null
) {

    companion object {

        fun from(user: FirebaseUser) = UserModel(
            id = FoundationConstants.EMPTY_LONG,
            fullName = user.displayName ?: FoundationConstants.EMPTY_STRING,
            email = user.email ?: FoundationConstants.EMPTY_STRING,
            status = UserStatusEnum.CREATED,
            createdAt = OffsetDateTime.now()
        )

    }

}