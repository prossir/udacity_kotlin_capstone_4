package paolo.udacity.auth.platform.views.common.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import paolo.udacity.auth.domain.use_case.SetCurrentUserUseCase
import paolo.udacity.auth.platform.views.common.mapper.UserMapper
import paolo.udacity.auth.platform.views.common.model.UserModel
import paolo.udacity.foundation.extensions.safeLaunch
import paolo.udacity.foundation.extensions.withDispatcher
import paolo.udacity.foundation.presentation.mapper.FailureMapper
import paolo.udacity.foundation.utils.Event
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewModel(
    private val setCurrentUserUseCase: SetCurrentUserUseCase,
    private val userMapper: UserMapper,
    private val failureMapper: FailureMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    @Inject
    constructor(setCurrentUserUseCase: SetCurrentUserUseCase, userMapper: UserMapper,
                failureMapper: FailureMapper) :
            this(setCurrentUserUseCase, userMapper, failureMapper, Dispatchers.IO)

    private val _actionState = MutableLiveData<AuthenticationActionState>()
    val actionState: LiveData<AuthenticationActionState>
        get() = _actionState

    fun authenticate() {
        _actionState.postValue(AuthenticationActionState.Authenticate(Event.newNavigationEvent()))
    }

    fun setCurrentUser(user: FirebaseUser) {
        val currentUser: UserModel = UserModel.from(user)
        viewModelScope.safeLaunch(::handleException) {
            withDispatcher(dispatcher) {
                setCurrentUserUseCase(userMapper.map(currentUser))
            }
        }
    }

    fun reportOnAuthenticationError(response: IdpResponse?) {
        when(response) {
            null -> { // the user canceled the sign-in flow using the back button
                handleException(Throwable("Authentication cancelled."))
                Timber.i("Login was cancelled by user.")
            }
            else -> {
                handleException(Throwable("Could not authenticate due to " +
                        "${response.error?.message ?: "unknown error"}."))
                Timber.i("Sign in unsuccessful FirebaseUI Error= " +
                        "${response.error?.errorCode}/ Error message= ${response.error?.message}")
            }
        }
    }

    private fun handleException(t: Throwable) {
        failureMapper.map(t).let {
            _actionState.postValue(AuthenticationActionState.Failure(it))
        }
    }

}