package paolo.udacity.auth.data.datasource.local

import org.threeten.bp.OffsetDateTime
import paolo.udacity.auth.domain.entity.User
import paolo.udacity.foundation.database.models.UserEntity
import paolo.udacity.foundation.database.providers.DaoProvider
import paolo.udacity.foundation.providers.DateTimeProvider


class UserLocalDatasource(
    daoProvider: DaoProvider,
    private val dateTimeProvider: DateTimeProvider<OffsetDateTime>
) {

    private val userDao = daoProvider.getUserDao()

    suspend fun findUserByEmail(currentUser: User): UserEntity? {
        return userDao.findByEmail(currentUser.email)
    }

    suspend fun createAndLogUser(currentUser: UserEntity): UserEntity {
        val newUser = UserEntity.from(currentUser)
        userDao.insertOrUpdate(newUser, dateTimeProvider)
        return newUser
    }

    suspend fun logoutCurrentUser() {
        userDao.logout()
    }


}