package com.bangkit.scantion.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bangkit.scantion.util.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity(tableName = Constants.TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
data class SkinCase(
    @PrimaryKey
    val id: String = "case-id-${UUID.randomUUID()}",
    val userId: String,
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