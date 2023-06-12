package com.bangkit.scantion.util

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.bangkit.scantion.model.CancerType
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class PredictImage(val context: Context, private val uri: Uri){
    fun predictImage(): Pair<String, Float> {
        val tfliteInterpreter = loadModelInterpreter()
        val inputImage = loadImage()
        val result = inputImage?.let { runInference(tfliteInterpreter, it) }
            ?: return Pair("Tidak terklasifikasi", 0f)

        val maxConfidenceIndex = result.first
        val maxConfidence = result.second
        val label = CancerType.listKey[maxConfidenceIndex]
        return Pair(label, maxConfidence)
    }

    private fun loadModelInterpreter(): Interpreter {
        val tfliteModel = loadModelFile()
        return Interpreter(tfliteModel)
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = context.assets.openFd(MODEL_PATH)
        val fileInputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = fileInputStream.channel
        val startOffset: Long = fileDescriptor.startOffset
        val declaredLength: Long = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadImage(): Bitmap? {
        return getBitmapFromUri(context, uri)
    }

    private fun runInference(interpreter: Interpreter, inputImage: Bitmap): Pair<Int, Float> {
        // Preprocess the input image
        val resizedImage = Bitmap.createScaledBitmap(inputImage, INPUT_IMAGE_SIZE, INPUT_IMAGE_SIZE, true)
        val inputBuffer = preprocessImage(resizedImage)

        // Run inference
        val output = Array(1) { FloatArray(NUM_CLASSES) }
        interpreter.run(inputBuffer, output)

        // Process the inference output
        val classIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        val confidence = output[0][classIndex]

        Log.d("Predict", "runInference: ${output[0][0]}, ${output[0][1]}, ${output[0][2]}")

        return Pair(classIndex, confidence)
    }

    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(BATCH_SIZE * INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE * PIXEL_SIZE * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_IMAGE_SIZE, INPUT_IMAGE_SIZE, true)

        val pixels = IntArray(INPUT_IMAGE_SIZE * INPUT_IMAGE_SIZE)
        resizedBitmap.getPixels(pixels, 0, INPUT_IMAGE_SIZE, 0, 0, INPUT_IMAGE_SIZE, INPUT_IMAGE_SIZE)

        for (pixel in pixels) {
            val r = (pixel shr 16) and 0xFF
            val g = (pixel shr 8) and 0xFF
            val b = pixel and 0xFF

            byteBuffer.putFloat((r.toFloat() / NORMALIZE))
            byteBuffer.putFloat((g.toFloat() / NORMALIZE))
            byteBuffer.putFloat((b.toFloat() / NORMALIZE))
        }

        byteBuffer.rewind()
        return byteBuffer
    }

    companion object{
        private const val MODEL_PATH = "scantion_model.tflite"
        private const val INPUT_IMAGE_SIZE = 100
        private const val NUM_CLASSES = 3
        private const val BATCH_SIZE = 1
        private const val PIXEL_SIZE = 3
        private const val NORMALIZE = 255.0f
    }
}

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