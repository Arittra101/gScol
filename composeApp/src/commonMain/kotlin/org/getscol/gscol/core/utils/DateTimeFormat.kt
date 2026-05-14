package org.getscol.gscol.core.utils// commonMain

sealed interface DateTimeFormat {

    /** Example: 2026 */
    data object YearOnly : DateTimeFormat

    /** Example: 10.05.2026 */
    data object DayMonthYear : DateTimeFormat

    /** Example: 10-05-2026 */
    data object DayMonthYearDash : DateTimeFormat

    /** Example: 2026-05-10 */
    data object IsoDate : DateTimeFormat

    /** Example: 09:45 AM */
    data object Time12Hour : DateTimeFormat

    /** Example: 09:45 */
    data object Time24Hour : DateTimeFormat

    /** Example: 10.05.2026 09:45 */
    data object DateTime : DateTimeFormat

    /**
     * Custom pattern if you want platform-specific formatting.
     * Examples:
     * "dd/MM/yyyy"
     * "MMM dd, yyyy"
     * "HH:mm:ss"
     */
    data class Custom(val pattern: String) : DateTimeFormat
}


expect object DateTimeProvider {
    fun now(format: DateTimeFormat): String
}