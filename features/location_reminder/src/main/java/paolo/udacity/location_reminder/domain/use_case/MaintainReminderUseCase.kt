package paolo.udacity.location_reminder.domain.use_case

import paolo.udacity.location_reminder.domain.entity.Reminder
import paolo.udacity.location_reminder.domain.repository.ReminderRepository


class MaintainReminderUseCase(private val reminderRepository: ReminderRepository) {

    suspend operator fun invoke(reminder: Reminder) {
        return reminderRepository.maintainReminder(reminder)
    }

}