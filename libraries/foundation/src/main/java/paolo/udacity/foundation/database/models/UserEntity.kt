package paolo.udacity.foundation.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.constants.FoundationConstants
import paolo.udacity.foundation.database.models.enums.UserStatusEnum


@Entity(
    tableName = UserEntity.TABLE_NAME,
    indices = [
        Index(value = [UserEntity.FIELD_ID])
    ]
)
data class UserEntity(
    @ColumnInfo(name = FIELD_ID)
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = FIELD_FULL_NAME)
    var fullName: String,
    @ColumnInfo(name = FIELD_EMAIL)
    var email: String,
    @ColumnInfo(name = FIELD_STATUS)
    val status : UserStatusEnum = UserStatusEnum.CREATED,
    @ColumnInfo(name = FIELD_CREATED_AT)
    var createdAt: OffsetDateTime?,
    @ColumnInfo(name = FIELD_UPDATED_AT)
    var updatedAt: OffsetDateTime? = createdAt
) {


    companion object {

        fun new() = UserEntity(
            id = FoundationConstants.EMPTY_LONG,
            fullName = FoundationConstants.EMPTY_STRING,
            email = FoundationConstants.EMPTY_STRING,
            status = UserStatusEnum.CREATED,
            createdAt = OffsetDateTime.now()
        )

        fun from(user: UserEntity) = UserEntity(
            id = user.id,
            fullName = user.fullName,
            email = user.email,
            status = UserStatusEnum.LOGGED,
            createdAt = user.createdAt
        )

        internal const val TABLE_NAME = "user"

        internal const val FIELD_ID = "id"
        internal const val FIELD_FULL_NAME = "full_name"
        internal const val FIELD_EMAIL = "email"
        internal const val FIELD_STATUS = "status"
        internal const val FIELD_CREATED_AT = "created_at"
        internal const val FIELD_UPDATED_AT = "updated_at"

    }

}