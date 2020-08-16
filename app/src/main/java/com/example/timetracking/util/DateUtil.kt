package com.example.timetracking.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime


fun formatDate(date: LocalDate): String {
    return String.format(
        "%s %s %s",
        date.dayOfMonth,
        date.month,
        date.year
    )
}

fun formatTime(time: LocalTime): String {
    return String.format(
        "%s:%s",
        time.hour,
        time.minute
    )
}

fun formatDateTime(dateTime: OffsetDateTime): String {
    return String.format(
        "%s, %s %s %s, %s:%s",
        dateTime.dayOfWeek,
        dateTime.dayOfMonth,
        dateTime.month,
        dateTime.year,
        dateTime.hour,
        dateTime.minute
    )
}
