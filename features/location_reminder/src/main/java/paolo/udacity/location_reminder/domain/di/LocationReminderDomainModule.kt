package paolo.udacity.location_reminder.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.threeten.bp.OffsetDateTime
import paolo.udacity.foundation.database.providers.DaoProvider
import paolo.udacity.foundation.providers.DateTimeProvider
import paolo.udacity.location_reminder.data.datasource.local.ReminderLocalDatasource
import paolo.udacity.location_reminder.data.mapper.local.PointOfInterestLocalMapper
import paolo.udacity.location_reminder.data.mapper.local.ReminderLocalMapper
import paolo.udacity.location_reminder.data.mapper.local.ReminderWithPointOfInterestLocalMapper
import paolo.udacity.location_reminder.data.repository.ReminderDataRepository
import paolo.udacity.location_reminder.domain.repository.ReminderRepository


@Module
@InstallIn(ActivityRetainedComponent::class)
object LocationReminderDomainModule {
    // Mappers
    // Local
    @Provides
    fun pointOfInterestLocalMapper() = PointOfInterestLocalMapper()

    @Provides
    fun providesReminderLocalMapper(pointOfInterestLocalMapper: PointOfInterestLocalMapper) =
        ReminderLocalMapper(pointOfInterestLocalMapper)

    @Provides
    fun providesStructuredReminderLocalMapper() = ReminderWithPointOfInterestLocalMapper()

    // Datasource
    // Local
    @Provides
    fun providesReminderLocalDatasource(daoProvider: DaoProvider,
                                        dateTimeProvider: DateTimeProvider<OffsetDateTime>) =
        ReminderLocalDatasource(daoProvider, dateTimeProvider)

    // Repository
    @ActivityRetainedScoped
    @Provides
    fun providesReminderRepository(
        reminderLocalDatasource: ReminderLocalDatasource,
        reminderLocalMapper: ReminderLocalMapper,
        structuredReminderLocalMapper: ReminderWithPointOfInterestLocalMapper
    ) : ReminderRepository =
        ReminderDataRepository(reminderLocalDatasource, reminderLocalMapper,
            structuredReminderLocalMapper)

}