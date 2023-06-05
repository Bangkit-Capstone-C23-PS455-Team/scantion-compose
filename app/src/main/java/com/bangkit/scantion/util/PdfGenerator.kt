package com.bangkit.scantion.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
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
        textSize = 18f
    }

    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page: PdfDocument.Page = pdfDocument.startPage(pageInfo)
    val canvas: Canvas = page.canvas

    // Set background color
    canvas.drawColor(Color.WHITE)

    // Add header text and logo
    val headerText = "Scantion - A Skin Cancer Detection Application"
    val logoBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img_card_examination)
    val logoWidth = 100
    val logoHeight = (logoBitmap.height.toFloat() / logoBitmap.width.toFloat() * logoWidth).toInt()

    val headerTextWidth = headerPaint.measureText(headerText)
    val headerTextHeight = headerPaint.fontMetrics.descent - headerPaint.fontMetrics.ascent

    val headerHeight = maxOf(logoHeight, headerTextHeight.toInt()) + 20

    val logoLeft = (pageWidth - (headerTextWidth + 20 + logoWidth)) / 2
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