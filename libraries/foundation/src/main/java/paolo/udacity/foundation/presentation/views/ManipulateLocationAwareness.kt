package paolo.udacity.foundation.presentation.views

import android.location.Location


interface ManipulateLocationAwareness {

    fun onLocationEnabled()
    fun onLocationChangedRecorded(location: Location)

}