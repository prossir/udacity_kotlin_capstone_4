package paolo.udacity.foundation.database.providers

import paolo.udacity.foundation.database.daos.PointOfInterestDao
import paolo.udacity.foundation.database.daos.ReminderDao
import paolo.udacity.foundation.database.daos.UserDao


class DaoProvider(private val database: DatabaseProvider) {

    fun getUserDao(): UserDao = database.getInstance().userDao()
    fun getReminderDao(): ReminderDao = database.getInstance().reminderDao()
    fun getPointOfInterestDao(): PointOfInterestDao = database.getInstance().pointOfInterestDao()

}