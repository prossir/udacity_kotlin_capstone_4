package paolo.udacity.location_reminder.features.views.common.views

import paolo.udacity.foundation.presentation.model.FailureModel


sealed class LocationUiState {

    object MapAndRemindersLoaded: LocationUiState()
    object DismissMaintainReminder: LocationUiState()
    data class Failure(val failure: FailureModel) : LocationUiState()

}