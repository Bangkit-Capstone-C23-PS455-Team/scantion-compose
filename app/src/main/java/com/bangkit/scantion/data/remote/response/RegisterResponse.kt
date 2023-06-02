package com.bangkit.scantion.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("result")
	val result: String? = null,

	val error: Boolean = result != null
)
