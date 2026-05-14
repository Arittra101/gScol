package org.getscol.gscol.core.utils// androidMain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual object DateTimeProvider {

    actual fun now(format: DateTimeFormat): String {
        val pattern = when (format) {
            DateTimeFormat.YearOnly -> "yyyy"
            DateTimeFormat.DayMonthYear -> "dd.MM.yyyy"
            DateTimeFormat.DayMonthYearDash -> "dd-MM-yyyy"
            DateTimeFormat.IsoDate -> "yyyy-MM-dd"
            DateTimeFormat.Time12Hour -> "hh:mm a"
            DateTimeFormat.Time24Hour -> "HH:mm"
            DateTimeFormat.DateTime -> "dd.MM.yyyy HH:mm"
            is DateTimeFormat.Custom -> format.pattern
        }

        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(Date())
    }
}