package paolo.udacity.auth.platform.views.common.views

import paolo.udacity.foundation.presentation.model.FailureModel
import paolo.udacity.foundation.utils.Event


sealed class AuthenticationActionState {

    data class Authenticate(val event: Event<Long>): AuthenticationActionState()
    data class Failure(val failure: FailureModel): AuthenticationActionState()

}