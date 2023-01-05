package paolo.udacity.location_reminder.features.views.common.views

import android.Manifest
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import paolo.udacity.location_reminder.features.delegates.locationAwareMap.LocationAwareMapDelegate
import paolo.udacity.foundation.presentation.model.FailureModel
import paolo.udacity.location_reminder.features.delegates.locationAwareMap.LocationAwareMapDelegateImpl
import paolo.udacity.foundation.utils.PermissionUtils
import paolo.udacity.location_reminder.R
import paolo.udacity.location_reminder.databinding.ActivityLocationReminderBinding
import paolo.udacity.location_reminder.features.delegates.geofenceManagement.GeofenceManagementDelegate
import paolo.udacity.location_reminder.features.delegates.geofenceManagement.GeofenceManagementDelegateImpl
import paolo.udacity.location_reminder.features.views.common.models.ReminderModel
import paolo.udacity.location_reminder.features.views.maintain_reminder.views.MaintainReminderFragment
import timber.log.Timber


@AndroidEntryPoint
class LocationAwarenessReminderActivity:
    AppCompatActivity(),
    GeofenceManagementDelegate by GeofenceManagementDelegateImpl(),
    LocationAwareMapDelegate by LocationAwareMapDelegateImpl(),
    OnMapReadyCallback {

    private val viewModel by viewModels<LocationReminderViewModel>()
    private val viewStateObserver = Observer<LocationUiState> { state ->
        when (state) {
            is LocationUiState.MapAndRemindersLoaded -> setRemindersAsMarkers()
            is LocationUiState.DismissMaintainReminder -> dismissMaintainReminder()
            is LocationUiState.Failure -> reportErrorEvent(state.failure)
        }
    }
    private val maintainReminderFragment by lazy { MaintainReminderFragment.newInstance() }

    private lateinit var binding: ActivityLocationReminderBinding

    private lateinit var map: GoogleMap
    private var registeredReminders: HashMap<Long, Int> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        initUi()
        initDelegates()
    }

    private fun initDelegates() {
        initGeofenceManager(this)
    }

    private fun initObservers() {
        viewModel.actionState.observe(this, viewStateObserver)

        viewModel.mapAndRemindersAreReadyAndLoaded.observe(this@LocationAwarenessReminderActivity) {
            if(it) {
                viewModel.triggerSetRemindersEvent()
            }
        }
    }

    private fun initUi() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location_reminder)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setMap()
    }

    private fun setMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        viewModel.isMapVisuallyReady.postValue(true)
        this.map = map
        initLocationAwareManager(this, map)
        map.uiSettings.isZoomControlsEnabled = true
        setMapListeners(map)
        setMapStyle(map)
        updateCameraToLocation()
    }

    private fun setMapListeners(map: GoogleMap) {
        map.setOnMapLongClickListener { latLong ->
            viewModel.initReminderFrom(latLong)
            if(!maintainReminderFragment.isAdded) {
                maintainReminderFragment.show(supportFragmentManager, TAG_FRAGMENT_MAINTAIN_REMINDER)
            }
        }

        map.setOnMarkerClickListener {
            viewModel.initReminderFrom(it.tag as ReminderModel)
            if(!maintainReminderFragment.isAdded) {
                maintainReminderFragment.show(supportFragmentManager, TAG_FRAGMENT_MAINTAIN_REMINDER)
            }
            return@setOnMarkerClickListener false
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)).apply {
                if (!this) {
                    Timber.e("Style parsing failed.")
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e("Can't find style. Error: ", e)
        }
    }

    private fun updateCameraToLocation() {
        if(!checkLocationPermissionWasGrantedEnableLocation()) {
            requestAccessLocationPermission()
        }
        else {
            enableMyLocationIfPossible()
        }
    }

    private fun setRemindersAsMarkers() {
        viewModel.reminders.observe(this) {
            for(reminder in it) {
                createMarkerOptionsAndGeofenceIfIsNew(reminder)?.apply {
                    val marker = map.addMarker(this)?.apply {
                        this.tag = reminder
                    }

                    marker?.let { existingMarker ->
                        map.addCircle(
                            createCircleOptions(existingMarker.position, reminder.pointOfInterest.rangeRadius)
                        )
                    }
                }
            }
        }
    }

    private fun createMarkerOptionsAndGeofenceIfIsNew(reminder: ReminderModel): MarkerOptions? {
        return if(isMarkerRegisteredInMap(reminder.id)) {
            null
        } else {
            registerReminderRegistrationFlag(reminder.id)
            // create geofence
            createGeofenceFromReminder(reminder)
            // creates a marker in the map
            MarkerOptions()
                .position(LatLng(reminder.pointOfInterest.latitude, reminder.pointOfInterest.longitude))
                .title(reminder.title)
                .snippet(reminder.description)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
            // creates a circular zone to emulate the zone.
            // Instantiating CircleOptions to draw a circle around the marker
        }
    }

    private fun createCircleOptions(position: LatLng, rangeRadius: Double): CircleOptions {
        return CircleOptions().apply {
            this.center(position)
            this.radius(rangeRadius)
            this.strokeColor(Color.YELLOW)
            this.fillColor(0x30ff0000)
            this.strokeWidth(2f)
        }
        //TODO should also register the geofence here?
    }

    private fun isMarkerRegisteredInMap(reminderId: Long): Boolean {
        return registeredReminders[reminderId] != null
    }

    private fun registerReminderRegistrationFlag(reminderId: Long) {
        registeredReminders[reminderId] = REMINDER_REGISTERED_IN_MAP
    }

    private fun dismissMaintainReminder() {
        if(maintainReminderFragment.isAdded) {
            maintainReminderFragment.dismiss()
        }
    }

    private fun reportErrorEvent(failure: FailureModel) {
        Snackbar.make(findViewById(android.R.id.content), failure.messageForUser(this),
            Snackbar.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode != LocationAwareMapDelegateImpl.LOCATION_PERMISSION_REQUEST_CODE) {
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
        private const val REMINDER_REGISTERED_IN_MAP = 1

        private const val TAG_FRAGMENT_MAINTAIN_REMINDER = "FRAGMENT_BOTTOM_SHEET_MAINTAIN_REMINDER"
    }

}