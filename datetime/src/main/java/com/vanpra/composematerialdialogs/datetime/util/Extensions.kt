package com.vanpra.composematerialdialogs.datetime.util

import androidx.compose.ui.geometry.Offset
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

internal fun Float.getOffset(angle: Double): Offset =
    Offset((this * cos(angle)).toFloat(), (this * sin(angle)).toFloat())

internal val LocalDate.yearMonth: YearMonth
    get() = YearMonth.of(this.year, this.month)

internal val Month.fullLocalName: String
    get() = this.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())

internal val Month.shortLocalName: String
    get() = this.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())

internal val DayOfWeek.shortLocalName: String
    get() = this.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault())

internal val LocalTime.isAM: Boolean
    get() = this.hour in 0..11

internal val LocalTime.simpleHour: Int
    get() {
        val tempHour = this.hour % 12
        return if (tempHour == 0) 12 else tempHour
    }

internal fun LocalTime.toAM(): LocalTime = if (this.isAM) this else this.minusHours(12)
internal fun LocalTime.toPM(): LocalTime = if (!this.isAM) this else this.plusHours(12)

internal fun LocalTime.removeSeconds(): LocalTime = LocalTime.of(this.hour, this.second)

internal fun LocalDate.plusYearsStartDate(years: Long) =
    this.plusYears(years).with(TemporalAdjusters.firstDayOfYear())

internal fun LocalDate.plusYearsEndDate(years: Long) =
    this.plusYears(years).with(TemporalAdjusters.lastDayOfYear())

internal fun LocalDate.yearsDateRange(years: Long) =
    this.plusYearsStartDate(-years)..this.plusYearsEndDate(years)

internal fun LocalDate.plusMonthsStartDate(months: Long) =
    this.plusMonths(months).with(TemporalAdjusters.firstDayOfMonth())

internal fun LocalDate.plusMonthsEndDate(months: Long) =
    this.plusMonths(months).with(TemporalAdjusters.lastDayOfMonth())
