package com.bangkit.scantion.util

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.bangkit.scantion.R
import com.bangkit.scantion.model.SkinCase

@SuppressLint("QueryPermissionsNeeded")
fun saveToPdf(context: Context, skinCase: SkinCase, name: String) {
    val pageWidth = 792
    val pageHeight = 1120

    val pdfDocument = PdfDocument()

    val titlePaint = Paint().apply {
        color = Color.BLACK
        textSize = 36f
        typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
    }

    val bodyPaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 24f
    }

    val headerPaint = Paint().apply {
        color = Color.BLUE
        textSize = 24f
    }

    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page: PdfDocument.Page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    // Set background color
    canvas.drawColor(Color.WHITE)

    // Add header text and logo
    val headerText = "Scantion - A Skin Cancer Detection Application"
    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_playstore)
    val logoWidth = 70
    val logoHeight = (logoBitmap.height.toFloat() / logoBitmap.width.toFloat() * logoWidth).toInt()

    val headerTextWidth = headerPaint.measureText(headerText)
    val headerTextHeight = headerPaint.fontMetrics.descent - headerPaint.fontMetrics.ascent + 20

    val headerHeight = maxOf(logoHeight, headerTextHeight.toInt()) + 60

    val logoLeft = (pageWidth - (headerTextWidth + 20 + logoWidth)) / 2
    val logoTop = (headerHeight - logoHeight).toFloat() / 2 + 20

    canvas.drawText(headerText, logoLeft + logoWidth + 20, headerHeight / 2 + headerTextHeight / 2, headerPaint)
    canvas.drawBitmap(logoBitmap, null, RectF(logoLeft, logoTop, logoLeft + logoWidth, logoTop + logoHeight), null)

    // Add your content to the PDF page
    val titleText = "Skin Case Report"
    val bodyText = """
        ${skinCase.id}
        
        Patient Name: $name
        Body Part: ${skinCase.bodyPart}
        How Long: ${skinCase.howLong}
        Symptom: ${skinCase.symptom}
        Cancer Type: ${skinCase.cancerType}
        Accuracy: ${(skinCase.accuracy * 100).toInt()}%
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
    val side = 320
    val imageUri = Uri.parse(skinCase.photoUri)
    val bitmap = getBitmapFromUri(context, imageUri)
    val imageBitmap = bitmap?.let { cropToSquare(it, side) }

    // Draw the image on the PDF page
    if (imageBitmap != null) {
        val leftMargin = (pageWidth - side) / 2
        val topMargin = yPosition + 20f

        canvas.drawBitmap(
            imageBitmap,
            null,
            Rect(
                leftMargin,
                topMargin.toInt(),
                leftMargin + side,
                topMargin.toInt() + side
            ),
            null
        )
    }

    pdfDocument.finishPage(page)

    val pdfName = "scantion${skinCase.dateCreated}.pdf"

    val values = ContentValues().apply {
        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
        put(MediaStore.Downloads.DISPLAY_NAME, pdfName)
        put(MediaStore.Downloads.IS_PENDING, 1)
    }

    val resolver = context.contentResolver
    val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

    val pdfUri = resolver.insert(collection, values)

    pdfUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { outputStream ->
            pdfDocument.writeTo(outputStream)

            Toast.makeText(context, "PDF file generated: Download/$pdfName", Toast.LENGTH_SHORT).show()
            // Open the PDF file using an intent
            val openIntent = Intent(Intent.ACTION_VIEW)
            openIntent.setDataAndType(uri, "application/pdf")
            openIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP

            if (openIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(openIntent)
            } else {
                Toast.makeText(context, "No PDF viewer application found", Toast.LENGTH_SHORT).show()
            }
        }

        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }

    pdfDocument.close()

//    val pdfDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
//
//    pdfDocument.finishPage(page)
//
//    val fileName = "scantion_${skinCase.dateCreated}.pdf"
//    val file = File(pdfDir, fileName)
//
//    try {
//        pdfDocument.writeTo(FileOutputStream(file))
//
//        Toast.makeText(context, "PDF file generated in:\n${pdfDir}/$fileName", Toast.LENGTH_SHORT).show()
//    } catch (e: Exception) {
//        e.printStackTrace()
//
//        Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
//            .show()
//    }
//    pdfDocument.close()
}