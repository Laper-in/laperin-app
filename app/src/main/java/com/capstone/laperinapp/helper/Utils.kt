package com.capstone.laperinapp.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.capstone.laperinapp.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Locale

private const val MAXIMAL_SIZE = 2000000
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())

fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile("Laperin-${timeStamp}", ".jpg", filesDir)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun Bitmap.getRotatedBitmap(file: File): Bitmap? {
    val orientation = ExifInterface(file).getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun formatTelpNumber(telephoneNumber: String): String {
    val formated = if (telephoneNumber.startsWith("0")){
        telephoneNumber.replaceFirst("0", "62")
    } else if (telephoneNumber.startsWith("62")){
        telephoneNumber
    } else {
        "Nomor telepon diawali dengan 0 atau 62"
    }
    return formated
}

fun meterToKilometer(meter: Double): String {
    val kilometer = meter / 1000
    val formated = formatTwoDecimalPlaces(kilometer)
    return "$formated km"
}

fun formatTwoDecimalPlaces(value: Double): String {
    return String.format("%.2f", value)
}

fun formatDuration(context: Context,duration: String): String {
    val parts = duration.split(":")
    if (parts.size != 3) {
        return context.getString(R.string.format_waktu_tidak_valid)
    }

    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    val seconds = parts[2].toInt()

    val totalMinutes = hours * 60 + minutes
    val formattedHours = totalMinutes / 60
    val formattedMinutes = totalMinutes % 60

    return if (formattedHours > 0) {
        context.getString(R.string.estimasi_jam_menit, formattedHours, formattedMinutes)
    } else {
        context.getString(R.string.estimasi_menit, formattedMinutes)
    }
}

fun formatDurationList(context: Context,duration: String): String {
    val parts = duration.split(":")
    if (parts.size != 3) {
        return context.getString(R.string.format_waktu_tidak_valid)
    }

    val hours = parts[0].toInt()
    val minutes = parts[1].toInt()
    val seconds = parts[2].toInt()

    val totalMinutes = hours * 60 + minutes
    val formattedHours = totalMinutes / 60
    val formattedMinutes = totalMinutes % 60

    return if (formattedHours > 0) {
        context.getString(R.string.estimasi_jam, formattedHours)
    } else {
        context.getString(R.string.estimasi_menit_2, formattedMinutes)
    }
}