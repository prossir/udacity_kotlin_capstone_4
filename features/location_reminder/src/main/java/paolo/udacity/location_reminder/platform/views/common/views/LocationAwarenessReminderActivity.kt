package paolo.udacity.location_reminder.platform.views.common.views

import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import paolo.udacity.foundation.presentation.model.FailureModel
import paolo.udacity.foundation.presentation.views.LocationAwareActivity
import paolo.udacity.foundation.presentation.views.ManipulateLocationAwareness
import paolo.udacity.location_reminder.R
import paolo.udacity.location_reminder.databinding.ActivityLocationReminderBinding
import paolo.udacity.location_reminder.platform.views.common.models.ReminderModel
import paolo.udacity.location_reminder.platform.views.maintain_reminder.views.MaintainReminderFragment
import timber.log.Timber


@AndroidEntryPoint
class LocationAwarenessReminderActivity: LocationAwareActivity(), ManipulateLocationAwareness,
    OnMapReadyCallback {

    private val viewModel by viewModels<LocationReminderViewModel>()
    private val viewStateObserver = Observer<LocationActionState> { state ->
        when (state) {
            is LocationActionState.MapAndRemindersLoaded -> setRemindersAsMarkers()
            is LocationActionState.DismissMaintainReminder -> dismissMaintainReminder()
            is LocationActionState.Failure -> reportErrorEvent(state.failure)
        }
    }
    private val maintainReminderFragment by lazy { MaintainReminderFragment.newInstance() }

    private lateinit var binding: ActivityLocationReminderBinding

    private lateinit var map: GoogleMap
    private var registeredReminders: HashMap<Long, Int> = hashMapOf()
    private lateinit var geofencingClient: GeofencingClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        initUi()
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
        manipulateLocationAwareness = this

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
                createMarkerOptionsIfIsNew(reminder)?.apply {
                    map.addMarker(this)?.apply {
                        this.tag = reminder
                    }
                }
            }
        }
    }

    private fun createMarkerOptionsIfIsNew(reminder: ReminderModel): MarkerOptions? {
        return if(isMarkerRegisteredInMap(reminder.id)) {
            null
        } else {
            registerReminder(reminder.id)
            MarkerOptions()
                .position(LatLng(reminder.pointOfInterest.latitude, reminder.pointOfInterest.longitude))
                .title(reminder.title)
                .snippet(reminder.description)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        }
    }

    private fun isMarkerRegisteredInMap(reminderId: Long): Boolean {
        return registeredReminders[reminderId] != null
    }

    private fun registerReminder(reminderId: Long) {
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

    companion object {
        private const val REMINDER_REGISTERED_IN_MAP = 1

        private const val TAG_FRAGMENT_MAINTAIN_REMINDER = "FRAGMENT_BOTTOM_SHEET_MAINTAIN_REMINDER"
    }

    @SuppressLint("MissingPermission")
    override fun onLocationEnabled() {
        map.isMyLocationEnabled = true
    }

    override fun onLocationChangedRecorded(location: Location) {
        LatLng(location.latitude, location.longitude).let { userLocation ->
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14f))
        }
    }

}