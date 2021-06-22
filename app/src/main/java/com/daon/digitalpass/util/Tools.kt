package com.daon.digitalpass.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.text.SimpleDateFormat
import java.util.*

fun decodeBitmap(imageBase64: String?): Bitmap? {
    val decodedString = Base64.decode(imageBase64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}

fun formatDate(date: Date?): String {
    var result = ""
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    date?.let {
        result = dateFormat.format(date)
    }
    return result
}