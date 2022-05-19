package paolo.udacity.foundation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import paolo.udacity.foundation.database.converters.OffsetDateTimeConverter
import paolo.udacity.foundation.database.converters.ReminderStatusConverter
import paolo.udacity.foundation.database.converters.UserStatusConverter
import paolo.udacity.foundation.database.daos.PointOfInterestDao
import paolo.udacity.foundation.database.daos.ReminderDao
import paolo.udacity.foundation.database.daos.UserDao
import paolo.udacity.foundation.database.models.PointOfInterestEntity
import paolo.udacity.foundation.database.models.ReminderEntity
import paolo.udacity.foundation.database.models.UserEntity


@Database(
    entities = [
        UserEntity::class,
        ReminderEntity::class,
        PointOfInterestEntity::class
    ],
    version = AppDatabase.VERSION,
    exportSchema = false
)
@TypeConverters(
    OffsetDateTimeConverter::class,
    UserStatusConverter::class,
    ReminderStatusConverter::class
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun reminderDao(): ReminderDao
    abstract fun pointOfInterestDao(): PointOfInterestDao

    companion object {
        const val VERSION = 1
        const val NAME = "db"
    }

}