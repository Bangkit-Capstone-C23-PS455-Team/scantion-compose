package com.bangkit.scantion.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getDayFormat(dateString: String): String{
    if (dateString.isEmpty()) return ""
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(dateString,formatter ).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}