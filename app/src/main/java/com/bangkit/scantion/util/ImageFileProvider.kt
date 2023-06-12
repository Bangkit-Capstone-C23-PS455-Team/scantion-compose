package com.bangkit.scantion.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.bangkit.scantion.model.SavedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageFileProvider : FileProvider() {
    companion object {
        private const val prefixSelected = "selected_image"
        private const val prefixSaved = "saved_image"
        private const val extensionFile = "jpg"
        private const val authorityProvider = "image_provider"

        fun getImageUri(context: Context): Uri {
            val file = addImage(context)
            val authority = "${context.packageName}.$authorityProvider"
            return getUriForFile(context, authority, file)
        }

        private fun addImage(context: Context): File {
            val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File(imagesDir, "${prefixSelected}_${System.currentTimeMillis()}.$extensionFile")
        }

        fun savedImage(context: Context, imageUri: Uri, id: String): SavedImage? {
            val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val bitmap = getBitmapFromUri(context, imageUri)
                ?: return null

            val croppedBitmap = cropToSquare(bitmap, 480)

            val predictionResult = PredictImage(context, imageUri).predictImage()

            val savedImageFile = File(imagesDir, "${prefixSaved}_${id}.$extensionFile")

            try {
                val outputStream = FileOutputStream(savedImageFile)
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            deleteImageUnused(context)

            return SavedImage(
                getUriForFile(
                    context,
                    "${context.packageName}.$authorityProvider",
                    savedImageFile
                ),
                predictionResult.first,
                predictionResult.second
            )
        }

        fun deleteImageUnused(context: Context) {
            val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val selectedImages = imagesDir?.listFiles { file ->
                file.name.startsWith(prefixSelected)
            }
            selectedImages?.forEach { file ->
                file.delete()
            }
        }

        fun deleteImageByUri(context: Context, id: String) {
            val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val selectedImages = imagesDir?.listFiles { file ->
                file.name.startsWith("${prefixSaved}_${id}")
            }
            selectedImages?.forEach { file ->
                file.delete()
            }
        }
    }
}