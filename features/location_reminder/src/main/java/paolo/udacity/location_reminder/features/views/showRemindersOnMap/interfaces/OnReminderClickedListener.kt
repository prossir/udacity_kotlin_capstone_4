package paolo.udacity.location_reminder.features.views.showRemindersOnMap.interfaces

import paolo.udacity.location_reminder.features.views.common.models.ReminderModel


interface OnReminderClickedListener {

    fun onReminderClicked(reminder: ReminderModel)

}