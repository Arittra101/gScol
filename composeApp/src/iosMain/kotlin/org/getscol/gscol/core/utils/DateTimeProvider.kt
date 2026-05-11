package org.getscol.gscol.core.utils// iosMain

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

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

        val formatter = NSDateFormatter().apply {
            dateFormat = pattern
            locale = NSLocale.currentLocale
        }

        return formatter.stringFromDate(NSDate())
    }
}