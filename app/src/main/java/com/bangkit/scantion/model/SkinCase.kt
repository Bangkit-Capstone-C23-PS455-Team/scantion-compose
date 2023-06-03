package com.bangkit.scantion.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SkinCase(
    val id: Int? = null,
    val photoUri: String,
    val bodyPart: String,
    val howLong: String,
    val symptom: String,
    val cancerType: String,
    val accuracy: Float,
    val dateCreated: String = getDateCreated()
)

fun getDateCreated(): String {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}