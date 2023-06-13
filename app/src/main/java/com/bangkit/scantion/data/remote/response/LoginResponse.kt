package com.bangkit.scantion.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null
)
