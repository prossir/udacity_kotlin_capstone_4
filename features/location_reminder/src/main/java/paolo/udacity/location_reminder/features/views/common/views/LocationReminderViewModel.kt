package paolo.udacity.location_reminder.features.views.common.views

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import paolo.udacity.foundation.database.models.enums.ReminderStatusEnum
import paolo.udacity.foundation.extensions.safeLaunch
import paolo.udacity.foundation.extensions.withDispatcher
import paolo.udacity.foundation.presentation.mapper.FailureMapper
import paolo.udacity.location_reminder.domain.use_case.MaintainReminderUseCase
import paolo.udacity.location_reminder.domain.use_case.FindCurrentRemindersByLatestUseCase
import paolo.udacity.location_reminder.features.views.common.mappers.ReminderMapper
import paolo.udacity.location_reminder.features.views.common.models.ReminderModel
import javax.inject.Inject


@HiltViewModel
class LocationReminderViewModel @Inject constructor(
    private val maintainReminderUseCase: MaintainReminderUseCase,
    private val findCurrentRemindersByLatestUseCase: FindCurrentRemindersByLatestUseCase,
    private val reminderMapper: ReminderMapper,
    private val failureMapper: FailureMapper,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _actionState = MutableLiveData<LocationUiState>()
    val actionState: LiveData<LocationUiState>
        get() = _actionState

    private lateinit var _reminders: LiveData<List<ReminderModel>>
    val reminders: LiveData<List<ReminderModel>>
        get() = _reminders

    private val areRemindersLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    val isMapVisuallyReady: MutableLiveData<Boolean> = MutableLiveData(false)
    val mapAndRemindersAreReadyAndLoaded: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        observeMapReadiness()
        loadReminders()
    }

    private fun loadReminders() {
        viewModelScope.safeLaunch(::handleException) {
            _reminders = withDispatcher(dispatcher) {
                Transformations.map(findCurrentRemindersByLatestUseCase()) {
                    reminderMapper.map(it)
                }
            }
            areRemindersLoaded.postValue(true)
        }
    }

    private fun observeMapReadiness() {
        mapAndRemindersAreReadyAndLoaded.addSource(isMapVisuallyReady) {
            mapAndRemindersAreReadyAndLoaded.value = it && (areRemindersLoaded.value == true)
        }
        mapAndRemindersAreReadyAndLoaded.addSource(areRemindersLoaded) {
            mapAndRemindersAreReadyAndLoaded.value = (isMapVisuallyReady.value == true) && it
        }
    }

    fun triggerSetRemindersEvent() {
        _actionState.postValue(LocationUiState.MapAndRemindersLoaded)
    }

    // Maintain reminders
    val isBeingEdited: MutableLiveData<Boolean> = MutableLiveData(false)
    val editableReminder: LiveData<ReminderModel> = MutableLiveData(ReminderModel.new())

    fun initReminderFrom(latLong: LatLng) {
        isBeingEdited.postValue(false)
        editableReminder.value?.let {
            it.reset()
            it.pointOfInterest.setFromLatLng(latLong)
        }
    }

    fun createReminder() {
        saveReminder()
        dismissMaintainReminder()
    }

    fun initReminderFrom(reminder: ReminderModel) {
        isBeingEdited.postValue(true)
        editableReminder.value?.apply {
            setFromOtherReminder(reminder)
        }
    }

    fun updateReminder() {
        saveReminder()
    }

    fun deleteReminder() {
        editableReminder.value?.status = ReminderStatusEnum.DELETED
        saveReminder()
    }

    private fun saveReminder() {
        viewModelScope.safeLaunch(::handleException) {
            withDispatcher(dispatcher) {
                editableReminder.value?.apply {
                    maintainReminderUseCase(reminderMapper.reverseMap(this))
                }
            }
        }
    }

    // Dismiss maintenance view from screen
    fun dismissMaintainReminder() {
        _actionState.postValue(LocationUiState.DismissMaintainReminder)
    }

    private fun handleException(t: Throwable) {
        failureMapper.map(t).let {
            _actionState.postValue(LocationUiState.Failure(it))
        }
    }

    private val _currentMapStyle : MutableLiveData<Int> = MutableLiveData()
    val currentMapStyle: LiveData<Int> = _currentMapStyle
    fun setMapStyle(which: Int) {
        _currentMapStyle.postValue(which)
    }

    private val _remindersAreVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val remindersAreVisible: LiveData<Boolean> = _remindersAreVisible
    fun showRemindersAsList() {
        _remindersAreVisible.postValue(true)
    }

    fun hideRemindersAsList() {
        _remindersAreVisible.postValue(false)
    }

}