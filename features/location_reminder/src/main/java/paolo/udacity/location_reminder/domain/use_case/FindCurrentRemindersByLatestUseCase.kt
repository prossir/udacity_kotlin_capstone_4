package paolo.udacity.location_reminder.domain.use_case

import androidx.lifecycle.LiveData
import paolo.udacity.location_reminder.domain.entity.Reminder
import paolo.udacity.location_reminder.domain.repository.ReminderRepository


class FindCurrentRemindersByLatestUseCase(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(): LiveData<List<Reminder>> {
        return reminderRepository.findCurrentRemindersByLatest()
    }

}