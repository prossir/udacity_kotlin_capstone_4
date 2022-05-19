package paolo.udacity.auth.domain.use_case

import paolo.udacity.auth.domain.entity.User
import paolo.udacity.auth.domain.repository.AuthenticationRepository


class SetCurrentUserUseCase(private val authenticationRepository: AuthenticationRepository) {

    suspend operator fun invoke(newUser: User) : User {
        return authenticationRepository.setCurrentUser(newUser)
    }

}