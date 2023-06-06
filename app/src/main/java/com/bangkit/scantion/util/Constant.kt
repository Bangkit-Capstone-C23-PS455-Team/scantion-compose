package com.bangkit.scantion.util

import com.bangkit.scantion.model.SkinCase

object Constants {
    const val TABLE_NAME = "skin_cases"
    const val DATABASE_NAME = "skin_cases_database"

    fun List<SkinCase>?.orPlaceHolderList(): List<SkinCase> {
        fun placeHolderList(): List<SkinCase> {
            return listOf(SkinCase(id = "0", userId = "Tidak ada riwayat pemeriksaan", "", "Silahkan periksa terlebih dahulu", "", "", "", 0f))
        }
        return if (!this.isNullOrEmpty()){
            this
        } else placeHolderList()
    }
    val storyDetailPlaceHolder = SkinCase(id = "0", userId = "Cannot find story details", "", "Cannot find story details", "", "", "", 0f)
}