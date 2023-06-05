package com.bangkit.scantion.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.FileNotFoundException

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}

fun cropToSquare(bitmap: Bitmap, side: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    val dimension = if (width <= height) width else height

    val x = (width - dimension) / 2
    val y = (height - dimension) / 2

    val croppedBitmap = Bitmap.createBitmap(bitmap, x, y, dimension, dimension)

    return Bitmap.createScaledBitmap(croppedBitmap, side, side, false)
}