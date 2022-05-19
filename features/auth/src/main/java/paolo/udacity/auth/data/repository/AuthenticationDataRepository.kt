package paolo.udacity.auth.data.repository

import paolo.udacity.auth.data.datasource.local.UserLocalDatasource
import paolo.udacity.auth.data.mapper.local.UserLocalMapper
import paolo.udacity.auth.domain.entity.User
import paolo.udacity.auth.domain.repository.AuthenticationRepository
import paolo.udacity.foundation.database.models.UserEntity


/**
 * Doing this just for practising Hilt. I used firebaseAuth for the normal flow.
 * Sorry for the confusion!. And thanks. If you wish me to remove it I will just tell me!
 */
class AuthenticationDataRepository(
    private val userLocalDatasource: UserLocalDatasource,
    private val userLocalMapper: UserLocalMapper
): AuthenticationRepository {

    override suspend fun setCurrentUser(currentUser: User): User {
        var loggedUser : UserEntity = UserEntity.new()
        userLocalDatasource.findUserByEmail(currentUser)?.let {
            loggedUser = it
        } ?: kotlin.run {
            loggedUser = userLocalDatasource.createAndLogUser(
                userLocalMapper.reverseMap(currentUser)
            )
        }
        return userLocalMapper.map(loggedUser)
    }

    override suspend fun logout() {
        userLocalDatasource.logoutCurrentUser()
    }

}