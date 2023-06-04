package com.bangkit.scantion.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.scantion.R
import com.bangkit.scantion.model.SkinCase
import java.io.File
import java.io.FileOutputStream

@SuppressLint("QueryPermissionsNeeded")
fun saveToPdf(context: Context, skinCase: SkinCase) {
    val name = "aji"
    val pageWidth = 792
    val pageHeight = 1120

    val pdfDocument = PdfDocument()

    val titlePaint = Paint()
    val bodyPaint = Paint()
    val headerPaint = Paint()

    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page: PdfDocument.Page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    // Set background color
    canvas.drawColor(Color.WHITE)

    // Set title style
    titlePaint.color = Color.BLACK
    titlePaint.textSize = 36f
    titlePaint.typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)

    // Set body style
    bodyPaint.color = Color.DKGRAY
    bodyPaint.textSize = 24f

    // Set header style
    headerPaint.color = Color.BLUE
    headerPaint.textSize = 18f

    // Add header text and logo
    val headerText = "Scantion - A Skin Cancer Detection Application"
    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img_card_examination)
    val logoWidth = 100
    val logoHeight = (logoBitmap.height.toFloat() / logoBitmap.width.toFloat() * logoWidth).toInt()

    val headerTextWidth = headerPaint.measureText(headerText)
    val headerTextHeight = headerPaint.fontMetrics.descent - headerPaint.fontMetrics.ascent

    val headerHeight = maxOf(logoHeight, headerTextHeight.toInt()) + 20

    val logoLeft = (pageWidth - (headerTextWidth + 20 + logoWidth)).toFloat() / 2
    val logoTop = (headerHeight - logoHeight).toFloat() / 2

    canvas.drawText(headerText, logoLeft + logoWidth + 20, headerHeight / 2 + headerTextHeight / 2, headerPaint)
    canvas.drawBitmap(logoBitmap, null, RectF(logoLeft, logoTop, logoLeft + logoWidth, logoTop + logoHeight), null)

    // Add your content to the PDF page
    val titleText = "Skin Case Report"
    val bodyText = """
        Patient Name: $name
        Body Part: ${skinCase.bodyPart}
        How Long: ${skinCase.howLong}
        Symptom: ${skinCase.symptom}
        Cancer Type: ${skinCase.cancerType}
        Accuracy: ${skinCase.accuracy}
        Date Created: ${skinCase.dateCreated}
    """.trimIndent()

    // Add title text
    canvas.drawText(titleText, 20f, headerHeight + 60f, titlePaint)

    // Add body text
    val bodyLines = bodyText.split("\n")
    var yPosition = headerHeight + 100f
    for (line in bodyLines) {
        canvas.drawText(line.trim(), 20f, yPosition, bodyPaint)
        yPosition += 40f
    }

    // Load the image from the URI and compress it
    val imageUri = Uri.parse(skinCase.photoUri)
    val imageBitmap = uriToBitmap(imageUri, context)

    // Draw the image on the PDF page
    if (imageBitmap != null) {
        val imageWidth = 400
        val imageHeight = 400
        val leftMargin = (pageWidth - imageWidth) / 2
        val topMargin = yPosition + 20f

        canvas.drawBitmap(
            imageBitmap,
            null,
            Rect(
                leftMargin,
                topMargin.toInt(),
                leftMargin + imageWidth,
                topMargin.toInt() + imageHeight
            ),
            null
        )
    }

    pdfDocument.finishPage(page)

    val folderName = "Scantion"
    val folder = File(Environment.getExternalStorageDirectory(), folderName)
    if (!folder.exists()) {
        folder.mkdirs()
    }

    val fileName = "scantion_${skinCase.dateCreated}.pdf"
    val file = File(folder, fileName)

    try {
        pdfDocument.writeTo(FileOutputStream(file))

        Toast.makeText(context, "PDF file generated in:\n$folderName\\$fileName", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()

        Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
            .show()
    }
    pdfDocument.close()
}

private fun uriToBitmap(uri: Uri, context: Context): Bitmap? {
    val inputStream = context.contentResolver.openInputStream(uri)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val side = 320
    val bitmapImage = Bitmap.createScaledBitmap(bitmap, side, side, true)
    inputStream?.close()
    return bitmapImage
}

fun checkPermissions(context: Context): Boolean {
    val writeStoragePermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val readStoragePermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    return writeStoragePermission == PackageManager.PERMISSION_GRANTED && readStoragePermission == PackageManager.PERMISSION_GRANTED
}

fun requestPermission(activity: Activity) {
    ActivityCompat.requestPermissions(
        activity,
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), 101
    )
}