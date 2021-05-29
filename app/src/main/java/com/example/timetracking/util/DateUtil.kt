package com.example.timetracking.util

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import java.util.*


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

fun formatTime(time: Timestamp): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
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

fun timeStrToMillis(str: String): Long {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result * 1000L * if (parts.size == 2) 60L else 1L
}
