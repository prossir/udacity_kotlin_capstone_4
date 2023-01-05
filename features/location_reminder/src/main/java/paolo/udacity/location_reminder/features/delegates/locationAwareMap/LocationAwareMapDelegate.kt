package paolo.udacity.location_reminder.features.delegates.locationAwareMap

import android.location.Location
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.GoogleMap


interface LocationAwareMapDelegate {

    fun initLocationAwareManager(activity: FragmentActivity, map: GoogleMap)

    fun onLocationEnabled()
    fun onLocationChangedRecorded(location: Location)

    fun checkLocationPermissionWasGrantedEnableLocation() : Boolean
    fun requestAccessLocationPermission()
    fun enableMyLocationIfPossible() : Boolean

}