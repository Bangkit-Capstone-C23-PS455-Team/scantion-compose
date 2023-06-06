package com.bangkit.scantion.model

data class UserLog(
    var token: String = "",
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var age: Int = 0,
    var province: String = "",
    var city: String = ""
)

data class UserReg(
    var name: String,
    var email: String,
    var password: String,
    var age: Int,
    var province: String,
    var city: String
)