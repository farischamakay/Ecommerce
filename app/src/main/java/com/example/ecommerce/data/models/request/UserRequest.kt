package com.example.ecommerce.data.models.request

import com.google.gson.annotations.SerializedName

data class UserRequest(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("firebaseToken")
	val firebaseToken: String = "",

	@field:SerializedName("email")
	val email: String? = null
)
