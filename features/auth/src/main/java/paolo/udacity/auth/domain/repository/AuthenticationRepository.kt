package paolo.udacity.auth.domain.repository

import paolo.udacity.auth.domain.entity.User


interface AuthenticationRepository {

    suspend fun setCurrentUser(currentUser: User): User
    suspend fun logout()

}