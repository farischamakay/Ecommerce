package com.example.ecommerce.data.models.request

import com.google.gson.annotations.SerializedName

data class RefreshRequest(

	@field:SerializedName("token")
	val token: String? = null
)
