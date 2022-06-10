package paolo.udacity.foundation.presentation.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import paolo.udacity.foundation.utils.PermissionUtils
import timber.log.Timber


abstract class LocationAwareActivity: AppCompatActivity(), LocationListener {

    private lateinit var locationManager: LocationManager
    var manipulateLocationAwareness: ManipulateLocationAwareness? = null


    fun checkLocationPermissionWasGrantedEnableLocation() : Boolean {
        if(!enableMyLocationIfPossible()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                PermissionUtils.RationaleDialog.newInstance(LOCATION_PERMISSION_REQUEST_CODE, true)
                    .show(supportFragmentManager, DIALOG_REQUEST_LOCATION_PERMISSION)
                return false
            }
        }
        return false
    }

    fun enableMyLocationIfPossible() : Boolean {
        return if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {
            manipulateLocationAwareness?.onLocationEnabled()

            (getSystemService(Context.LOCATION_SERVICE) as LocationManager).let { locationManager ->
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

    fun requestAccessLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onLocationChanged(location: Location) {
        try {
            manipulateLocationAwareness?.onLocationChangedRecorded(location)
            locationManager.removeUpdates(this)
        } catch(e: Exception) {
            Timber.e(e)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)
            || PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            enableMyLocationIfPossible()
        }
        else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            // permissionDenied = true
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 12
        private const val DIALOG_REQUEST_LOCATION_PERMISSION = "DIALOG_REQUEST_LOCATION_PERMISSION"
    }

}