package paolo.udacity.location_reminder.features.delegates.geofenceManagement

import android.content.Context
import paolo.udacity.location_reminder.features.views.common.models.ReminderModel


interface GeofenceManagementDelegate {

    fun initGeofenceManager(context: Context)
    fun createGeofenceFromReminder(reminder: ReminderModel)
    fun removeGeofenceOfReminder(reminderId: Long)

}