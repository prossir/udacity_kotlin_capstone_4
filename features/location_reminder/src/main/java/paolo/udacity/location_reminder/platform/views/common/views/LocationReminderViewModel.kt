package paolo.udacity.location_reminder.platform.views.common.views

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import paolo.udacity.foundation.database.models.enums.ReminderStatusEnum
import paolo.udacity.foundation.extensions.safeLaunch
import paolo.udacity.foundation.extensions.withDispatcher
import paolo.udacity.foundation.presentation.mapper.FailureMapper
import paolo.udacity.location_reminder.domain.use_case.MaintainReminderUseCase
import paolo.udacity.location_reminder.domain.use_case.FindCurrentRemindersByLatestUseCase
import paolo.udacity.location_reminder.platform.views.common.mapper.ReminderMapper
import paolo.udacity.location_reminder.platform.views.common.models.ReminderModel
import javax.inject.Inject


@HiltViewModel
class LocationReminderViewModel @Inject constructor(
    private val maintainReminderUseCase: MaintainReminderUseCase,
    private val findCurrentRemindersByLatestUseCase: FindCurrentRemindersByLatestUseCase,
    private val reminderMapper: ReminderMapper,
    private val failureMapper: FailureMapper,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _actionState = MutableLiveData<LocationActionState>()
    val actionState: LiveData<LocationActionState>
        get() = _actionState

    private lateinit var _reminders: LiveData<List<ReminderModel>>
    val reminders: LiveData<List<ReminderModel>>
        get() = _reminders

    private val areRemindersLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    val isMapReady: MutableLiveData<Boolean> = MutableLiveData(false)
    val mapAndRemindersAreReadyAndLoaded: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        // initialize reminders
        viewModelScope.safeLaunch(::handleException) {
            _reminders = withDispatcher(dispatcher) {
                Transformations.map(findCurrentRemindersByLatestUseCase()) {
                    reminderMapper.map(it)
                }
            }
            areRemindersLoaded.postValue(true)
        }

        // set the observable mediator
        mapAndRemindersAreReadyAndLoaded.addSource(isMapReady) { it && areRemindersLoaded.value == true }
        mapAndRemindersAreReadyAndLoaded.addSource(areRemindersLoaded) { isMapReady.value == true && it }
    }

    fun triggerSetRemindersEvent() {
        _actionState.postValue(LocationActionState.MapAndRemindersLoaded)
    }

    // Maintain reminders
    val isBeingEdited: MutableLiveData<Boolean> = MutableLiveData(false)
    val editableReminder: LiveData<ReminderModel> = MutableLiveData(ReminderModel.new())

    fun initReminderFrom(latLong: LatLng) {
        isBeingEdited.postValue(false)
        editableReminder.value?.let {
            it.reset()
            it.pointOfInterest.latitude = latLong.latitude
            it.pointOfInterest.longitude = latLong.longitude
            it.pointOfInterest.rangeRadius = 3.0
        }
    }

    fun createReminder() {
        saveReminder()
    }

    fun initReminderFrom(reminder: ReminderModel) {
        isBeingEdited.postValue(true)
        editableReminder.value?.apply {
            set(reminder)
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

    private fun handleException(t: Throwable) {
        failureMapper.map(t).let {
            _actionState.postValue(LocationActionState.Failure(it))
        }
    }

}