package com.bangkit.scantion.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ComposeFileProvider : FileProvider() {
    companion object {
        fun getImageUri(context: Context): Uri {
            val file = addImage(context)
            val authority = "${context.packageName}.fileprovider"
            return getUriForFile(context, authority, file)
        }

        private fun addImage(context: Context): File {
            val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File(imagesDir, "selected_image_${System.currentTimeMillis()}.jpg")
        }

        fun savedImage(context: Context, imageUri: Uri): Uri? {
            val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val bitmap = getBitmapFromUri(context, imageUri)
                ?:
                return null

            val croppedBitmap = cropToSquare(bitmap)
            val savedImageFile = File(imagesDir, "saved_image_${System.currentTimeMillis()}.jpg")

            try {
                val outputStream = FileOutputStream(savedImageFile)
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            deleteImageUnused(imagesDir)
            return getUriForFile(context, "${context.packageName}.fileprovider", savedImageFile)
        }

        private fun deleteImageUnused(imagesDir: File?) {
            val selectedImages = imagesDir?.listFiles { file ->
                file.name.startsWith("selected_image_")
            }
            selectedImages?.forEach { file ->
                file.delete()
            }
        }

        private fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
            return try {
                val inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                null
            }
        }

        private fun cropToSquare(bitmap: Bitmap): Bitmap {
            val side = 480
            val width = bitmap.width
            val height = bitmap.height

            val dimension = if (width <= height) width else height

            val x = (width - dimension) / 2
            val y = (height - dimension) / 2

            val croppedBitmap = Bitmap.createBitmap(bitmap, x, y, dimension, dimension)

            return Bitmap.createScaledBitmap(croppedBitmap, side, side, false)
        }
    }
}