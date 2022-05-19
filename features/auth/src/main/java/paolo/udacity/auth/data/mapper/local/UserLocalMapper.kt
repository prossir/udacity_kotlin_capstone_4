package paolo.udacity.auth.data.mapper.local

import paolo.udacity.auth.domain.entity.User
import paolo.udacity.foundation.database.models.UserEntity
import paolo.udacity.foundation.mappers.Mapper


class UserLocalMapper : Mapper<UserEntity, User>()  {

    override fun reverseMap(value: User) = UserEntity(
        id = value.id,
        fullName = value.fullName,
        email = value.email,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt
    )

    override fun map(value: UserEntity) = User(
        id = value.id,
        fullName = value.fullName,
        email = value.email,
        status = value.status,
        createdAt = value.createdAt,
        updatedAt = value.updatedAt
    )

}