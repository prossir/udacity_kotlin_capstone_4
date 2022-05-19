package paolo.udacity.foundation.database.converters

import androidx.room.TypeConverter
import paolo.udacity.foundation.database.models.enums.UserStatusEnum


class UserStatusConverter {

    @TypeConverter
    fun fromUserStatus(userStatus: UserStatusEnum): String {
        return userStatus.name
    }

    @TypeConverter
    fun toUserStatus(userStatus: String): UserStatusEnum {
        return UserStatusEnum.valueOf(userStatus)
    }

}