package com.capstone.laperinapp.helper

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile("Laperin-${timeStamp}", ".jpg", filesDir)
}

fun meterToKilometer(meter: Double): String {
    val kilometer = meter / 1000
    val formated = formatTwoDecimalPlaces(kilometer)
    return "$formated km"
}

fun formatTwoDecimalPlaces(value: Double): String {
    return String.format("%.2f", value)
}