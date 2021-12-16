package com.project.simplecoffee.utils.common

import java.time.LocalDate
import java.util.*

object CalendarHelper {
    private val cal = Calendar.getInstance()

    fun getThisWeek(): Pair<LocalDate, LocalDate> {
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        val from = LocalDate.of(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
        val to = from.plusDays(6)
        return Pair(from, to)
    }

    fun getThisMonth(): Pair<LocalDate, LocalDate> {
        val from = LocalDate.of(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            1
        )
        val to = from.withDayOfMonth(
            cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
        return Pair(from, to)
    }

    fun getThisYear() = Pair(
        LocalDate.of(
            cal.get(Calendar.YEAR),
            1,
            1
        ),
        LocalDate.of(
            cal.get(Calendar.YEAR),
            12,
            31
        )
    )
}