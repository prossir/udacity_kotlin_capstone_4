package paolo.udacity.location_reminder.features.views.showRemindersOnMap.views

import android.Manifest
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.annotation.RawRes
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import paolo.udacity.foundation.presentation.model.FailureModel
import paolo.udacity.foundation.utils.PermissionUtils
import paolo.udacity.location_reminder.R
import paolo.udacity.location_reminder.databinding.FragmentShowRemindersOnMapBinding
import paolo.udacity.location_reminder.features.delegates.geofenceManagement.GeofenceManagementDelegate
import paolo.udacity.location_reminder.features.delegates.geofenceManagement.GeofenceManagementDelegateImpl
import paolo.udacity.location_reminder.features.delegates.locationAwareMap.LocationAwareMapDelegate
import paolo.udacity.location_reminder.features.delegates.locationAwareMap.LocationAwareMapDelegateImpl
import paolo.udacity.location_reminder.features.views.common.models.ReminderModel
import paolo.udacity.location_reminder.features.views.common.views.LocationReminderViewModel
import paolo.udacity.location_reminder.features.views.common.views.LocationUiState
import paolo.udacity.location_reminder.features.views.maintainReminder.views.MaintainReminderFragment
import paolo.udacity.location_reminder.features.views.showRemindersOnMap.adapters.RemindersAdapter
import paolo.udacity.location_reminder.features.views.showRemindersOnMap.interfaces.OnReminderClickedListener
import timber.log.Timber


@AndroidEntryPoint
class ShowRemindersOnMapFragment:
    Fragment(),
    OnReminderClickedListener,
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

    private lateinit var binding: FragmentShowRemindersOnMapBinding
    private lateinit var map: GoogleMap
    private var registeredReminders: HashMap<Long, Int> = hashMapOf()
    private val adapter = RemindersAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_show_reminders_on_map, container, false)
        initObservers()
        initUi()
        initDelegates()
        return binding.root
    }

    private fun initObservers() {
        observeActionStates()
        observeIfMapAndRemindersAreReady()
        observeCurrentMapStyleChanges()
    }

    private fun observeActionStates() {
        viewModel.actionState.observe(viewLifecycleOwner, viewStateObserver)
    }

    private fun observeIfMapAndRemindersAreReady() {
        viewModel.mapAndRemindersAreReadyAndLoaded.observe(viewLifecycleOwner) {
            if(it) {
                viewModel.triggerSetRemindersEvent()
            }
        }
    }

    private fun observeCurrentMapStyleChanges() {
        viewModel.currentMapStyle.observe(viewLifecycleOwner) {
            when(it) {
                0 -> setMapStyle(map, R.raw.map_night)
                1 -> setMapStyle(map, R.raw.map_default)
            }
        }
    }

    private fun initUi() {
        initializeDataBinding()
        setTitle()
        setMenu()
        setMap()
    }

    private fun initializeDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.adapter = adapter
    }

    private fun setTitle() {
        activity?.title = "Reminders"
    }

    private fun setMenu() {
        activity?.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_map, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_show_map_style_chooser -> {
                        showMapStyleOptions()
                        true
                    }
                    R.id.action_list_reminders -> {
                        showReminders()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showMapStyleOptions() {
        val dialog = ChooseMapStyleDialogFragment()
        dialog.show(childFragmentManager, CHOOSE_MAP_STYLE_FRAGMENT_TAG)
    }

    private fun showReminders() {
        viewModel.showRemindersAsList()
    }

    private fun setMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initDelegates() {
        context?.let {
            initGeofenceManager(it)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        viewModel.isMapVisuallyReady.postValue(true)
        this.map = map
        initLocationAwareManager(requireActivity(), map)
        map.uiSettings.isZoomControlsEnabled = true
        setMapListeners(map)
        setMapStyle(map)
        updateCameraToLocation()
    }

    private fun setMapListeners(map: GoogleMap) {
        map.setOnMapLongClickListener { latLong ->
            viewModel.initReminderFrom(latLong)
            if(!maintainReminderFragment.isAdded) {
                maintainReminderFragment.show(childFragmentManager, MAINTAIN_REMINDER_FRAGMENT_TAG)
            }
        }

        map.setOnMarkerClickListener {
            viewModel.initReminderFrom(it.tag as ReminderModel)
            if(!maintainReminderFragment.isAdded) {
                maintainReminderFragment.show(childFragmentManager, MAINTAIN_REMINDER_FRAGMENT_TAG)
            }
            return@setOnMarkerClickListener false
        }
    }

    private fun setMapStyle(map: GoogleMap, @RawRes optionalStyle: Int = R.raw.map_night) {
        try {
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), optionalStyle)
            )
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
            adapter.submitList(it)
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
        context?.let {
            Snackbar.make(binding.root, failure.messageForUser(it),
                Snackbar.LENGTH_LONG).show()
        }
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

    override fun onReminderClicked(reminder: ReminderModel) {
        viewModel.hideRemindersAsList()
        LatLng(reminder.pointOfInterest.latitude, reminder.pointOfInterest.longitude).apply {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(this, 14f))
        }
    }

    companion object {
        private const val REMINDER_REGISTERED_IN_MAP = 1

        private const val MAINTAIN_REMINDER_FRAGMENT_TAG = "FRAGMENT_BOTTOM_SHEET_MAINTAIN_REMINDER"

        private const val CHOOSE_MAP_STYLE_FRAGMENT_TAG = "CHOOSE_MAP_STYLE_FRAGMENT_TAG"
    }

}