package paolo.udacity.foundation.presentation.mapper

import paolo.udacity.foundation.R
import paolo.udacity.foundation.exceptions.NetworkConnectionException
import paolo.udacity.foundation.mappers.SingleMapper
import paolo.udacity.foundation.presentation.model.FailureModel


class FailureMapper: SingleMapper<Throwable, FailureModel>() {

    override fun map(value: Throwable) = when (value) {
        is NetworkConnectionException -> FailureModel(
            title = R.string.error_no_internet_connection_title,
            message = R.string.error_no_internet_connection_description,
            icon = R.drawable.ic_error_network,
            origin = value
        )
        else -> FailureModel(
            title = R.string.error_ups,
            message = R.string.error_an_error_occurred,
            icon = FailureModel.NONE,
            origin = value
        )
    }

}