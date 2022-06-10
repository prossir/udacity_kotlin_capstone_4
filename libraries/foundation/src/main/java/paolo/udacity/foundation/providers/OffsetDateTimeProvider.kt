package paolo.udacity.foundation.providers

import org.threeten.bp.*
import javax.inject.Inject


class OffsetDateTimeProvider @Inject constructor() : DateTimeProvider<OffsetDateTime> {
    override fun now(): OffsetDateTime = OffsetDateTime.now(ZoneOffset.systemDefault())
    fun nowLocalDate(): LocalDate = now().toLocalDate()
    fun nowLocalDateTime(): LocalDateTime = now().toLocalDateTime()
    fun nowLocalTime(): LocalTime = now().toLocalTime()
    fun nowZoneOffset(): ZoneOffset = now().toOffsetTime().offset
}
