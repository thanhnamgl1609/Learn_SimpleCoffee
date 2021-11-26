package com.project.simplecoffee.common

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.firebase.Timestamp
import com.project.simplecoffee.constant.CustomConstant
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
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

fun LocalDate.toTimestamp(zone: ZoneOffset = ZoneOffset.UTC): Timestamp {
    val localDateTime = atStartOfDay()
    return localDateTime.toTimestamp(zone)
}

fun LocalDateTime.toTimestamp(zone: ZoneOffset = ZoneOffset.UTC) =
    Timestamp(toEpochSecond(zone), nano)

fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(CustomConstant.DATE_FORMAT))

@SuppressLint("SimpleDateFormat")
fun Timestamp.toCustomString(format: String = CustomConstant.DATE_FORMAT): String {
    val sfd = SimpleDateFormat(format)
    return sfd.format(toDate())
}

fun Timestamp.getDayOfWeek(zone: ZoneOffset = ZoneOffset.UTC): String {
    val localDateTime = LocalDateTime.ofEpochSecond(seconds, nanoseconds, zone)
    return localDateTime.dayOfWeek.name
}

fun String.toBitMap(): Bitmap? =
    BitmapFactory.decodeStream(URL(this).openStream())