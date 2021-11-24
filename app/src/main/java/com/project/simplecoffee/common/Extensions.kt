package com.project.simplecoffee.common

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.google.firebase.Timestamp
import com.project.simplecoffee.constant.CustomConstant
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

internal fun Activity.makeToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}

internal fun Date?.toCustomString(): String {
    if (this == null)
        return "null"
    val sdf = SimpleDateFormat(CustomConstant.DATE_FORMAT_CONST)
    return sdf.format(this).toString()
}

fun LocalDate.toTimestamp(): Timestamp {
    val localDateTime = atStartOfDay()
    return Timestamp(localDateTime.second.toLong(), localDateTime.nano)
}

fun String.toLocalDate(): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(CustomConstant.DATE_FORMAT_CONST))

fun String.toBitMap(): Bitmap? =
    BitmapFactory.decodeStream(URL(this).openStream())