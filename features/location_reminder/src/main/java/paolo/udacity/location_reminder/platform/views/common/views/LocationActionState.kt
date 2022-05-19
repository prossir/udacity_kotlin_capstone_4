package paolo.udacity.location_reminder.platform.views.common.views

import paolo.udacity.foundation.presentation.model.FailureModel


sealed class LocationActionState {

    object MapAndRemindersLoaded: LocationActionState()
    data class Failure(val failure: FailureModel) : LocationActionState()

}