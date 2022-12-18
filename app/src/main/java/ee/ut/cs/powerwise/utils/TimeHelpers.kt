package ee.ut.cs.powerwise.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjuster

class TimeHelpers {
    companion object {
        var timeFormat: String = "HH:mm"
        var dateFormat: String = "dd.MM.yyyy"

        /**
         * Converts UNIX timestamp to ZonedDateTime of UTC timezone
         */
        fun getToZonedDateTime(time: Long): ZonedDateTime {
            return ZonedDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.systemDefault())
        }

        fun formatUnixTime(time: Long, format: String): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)
            return formatter.format(
                ZonedDateTime.ofInstant(
                    Instant.ofEpochSecond(time),
                    ZoneId.systemDefault()
                )
            )
        }


        fun getTodayMinMaxTime(adjuster: TemporalAdjuster): ZonedDateTime {
            return ZonedDateTime.now().with(adjuster)
                .withZoneSameInstant(ZoneId.of("UTC"))
        }

        /**
         *  Converts UNIX timestamp to a specific time format.
         *  Default format is HH:mm
         */
        fun getTimeToString(time: Long, format: String?): String {
            return formatUnixTime(time, format ?: timeFormat)
        }

        /**
         *  Converts UNIX timestamp to a specific date format.
         *  Default format is dd.MM.yyyy
         */
        fun getDateToString(time: Long, format: String?): String {
            return formatUnixTime(time, format ?: dateFormat)
        }

        fun getCurrentHr(): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
            return formatter.format(ZonedDateTime.now())
        }
    }

}