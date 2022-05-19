package paolo.udacity.auth.platform.views.common.mapper

import paolo.udacity.auth.domain.entity.User
import paolo.udacity.auth.platform.views.common.model.UserModel
import paolo.udacity.foundation.mappers.Mapper


class UserMapper : Mapper<UserModel, User>() {

    override fun reverseMap(value: User) = UserModel(
        id = value.id,
        fullName = value.fullName,
        email = value.email,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt
    )

    override fun map(value: UserModel) = User(
        id = value.id,
        fullName = value.fullName,
        email = value.email,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt
    )

}