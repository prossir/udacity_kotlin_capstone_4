package paolo.udacity.location_reminder.features.delegates.locationAwareMap

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import paolo.udacity.foundation.utils.PermissionUtils
import timber.log.Timber


class LocationAwareMapDelegateImpl: LocationListener, LocationAwareMapDelegate {

    private lateinit var locationManager: LocationManager
    private lateinit var activity: FragmentActivity
    private lateinit var map: GoogleMap

    override fun initLocationAwareManager(activity: FragmentActivity, map: GoogleMap) {
        this.activity = activity
        this.map = map
    }

    override fun onLocationEnabled() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        map.isMyLocationEnabled = true
    }

    override fun onLocationChangedRecorded(location: Location) {
        LatLng(location.latitude, location.longitude).let { userLocation ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
        }
    }


    override fun checkLocationPermissionWasGrantedEnableLocation() : Boolean {
        if(!enableMyLocationIfPossible()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                PermissionUtils.RationaleDialog.newInstance(LOCATION_PERMISSION_REQUEST_CODE, true)
                    .show(activity.supportFragmentManager, DIALOG_REQUEST_LOCATION_PERMISSION)
                return false
            }
        }
        return false
    }

    override fun enableMyLocationIfPossible() : Boolean {
        return if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            onLocationEnabled()

            (activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager).let { locationManager ->
                this.locationManager = locationManager
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, /*min Time in MS*/
                    12000L, /* minimum distance in meters*/
                    5.0f,
                    this
                )
            }
            true
        }
        else {
            false
        }
    }

    override fun requestAccessLocationPermission() {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onLocationChanged(location: Location) {
        try {
            onLocationChangedRecorded(location)
            locationManager.removeUpdates(this)
        } catch(e: Exception) {
            Timber.e(e)
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 12
        private const val DIALOG_REQUEST_LOCATION_PERMISSION = "DIALOG_REQUEST_LOCATION_PERMISSION"
    }

}