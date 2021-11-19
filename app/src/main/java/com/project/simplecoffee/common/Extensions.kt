package com.project.simplecoffee.common
import android.app.Activity
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

internal fun Activity.makeToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}

internal fun Date?.toCustomString() : String {
    if (this == null)
        return "null"
    val sdf = SimpleDateFormat("MM/dd/yyyy")
    return sdf.format(this).toString()
}