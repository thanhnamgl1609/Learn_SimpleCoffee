package com.project.simplecoffee.utils.common

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.firebase.Timestamp
import com.project.simplecoffee.utils.constant.CustomConstant
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


internal fun Activity.makeToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}

internal fun LocalDate.toCustomString(pattern: String = CustomConstant.DATE_FORMAT): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)

}

internal fun LocalTime.toCustomString(pattern: String = CustomConstant.TIME_FORMAT): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)

}

fun LocalDate.toTimestamp(zone: ZoneOffset = ZoneOffset.UTC): Timestamp {
    val localDateTime = atStartOfDay()
    return localDateTime.toTimestamp(zone)
}

fun LocalDateTime.toTimestamp(zone: ZoneOffset = ZoneOffset.UTC) =
    Timestamp(toEpochSecond(zone), nano)

fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(CustomConstant.DATE_FORMAT))

@SuppressLint("SimpleDateFormat")
fun LocalDateTime.toCustomString(format: String = CustomConstant.DATE_FORMAT): String =
    format(DateTimeFormatter.ofPattern(format))

fun Timestamp.toLocalDateTime(zone: ZoneOffset = ZoneOffset.UTC): LocalDateTime =
    LocalDateTime.ofEpochSecond(seconds, nanoseconds, zone)


fun Timestamp.toLocalDate(zone: ZoneOffset = ZoneOffset.UTC): LocalDate =
    toLocalDateTime().toLocalDate()

fun Timestamp.getDayOfWeek(zone: ZoneOffset = ZoneOffset.UTC): String {
    return toLocalDateTime(zone).dayOfWeek.name
}

fun Double.round(numOfDigits: Int): Double =
    String.format("%.$numOfDigits" + "f", this).toDouble()