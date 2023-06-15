package com.bangkit.scantion.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun getDayFormat(input: String): String{
    if (input.isEmpty()) return ""
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(input,formatter ).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}