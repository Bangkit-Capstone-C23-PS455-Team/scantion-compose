package com.bangkit.scantion.model

data class User(
    var name: String,
    var email: String,
    var password: String,
    var age: Int,
    var province: String,
    var city: String
)