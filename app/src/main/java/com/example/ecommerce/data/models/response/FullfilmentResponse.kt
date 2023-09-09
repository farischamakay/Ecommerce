package com.example.ecommerce.data.models.response

import com.google.gson.annotations.SerializedName

data class FullfilmentResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: FulfillmentDetail? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class FulfillmentDetail(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("invoiceId")
	val invoiceId: String? = null,

	@field:SerializedName("payment")
	val payment: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
