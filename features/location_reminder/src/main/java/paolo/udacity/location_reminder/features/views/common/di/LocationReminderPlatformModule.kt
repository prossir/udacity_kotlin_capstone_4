package paolo.udacity.location_reminder.features.views.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import paolo.udacity.location_reminder.domain.repository.ReminderRepository
import paolo.udacity.location_reminder.domain.use_case.FindCurrentRemindersByLatestUseCase
import paolo.udacity.location_reminder.domain.use_case.MaintainReminderUseCase
import paolo.udacity.location_reminder.features.views.common.mapper.PointOfInterestMapper
import paolo.udacity.location_reminder.features.views.common.mapper.ReminderMapper


@Module
@InstallIn(ViewModelComponent::class)
object LocationReminderPlatformModule {

    // Use Cases
    @ViewModelScoped
    @Provides
    fun providesFindCurrentRemindersByLatestUseCase(reminderRepository: ReminderRepository) =
        FindCurrentRemindersByLatestUseCase(reminderRepository)

    @ViewModelScoped
    @Provides
    fun providesMaintainReminderUseCase(reminderRepository: ReminderRepository) =
        MaintainReminderUseCase(reminderRepository)

    // Mappers
    @Provides
    fun providesPointOfInterestMapper() = PointOfInterestMapper()

    @Provides
    fun providesReminderMapper(pointOfInterestMapper: PointOfInterestMapper) =
        ReminderMapper(pointOfInterestMapper)

    // Dispatcher
    @Provides
    fun providesDispatcher() = Dispatchers.IO

}