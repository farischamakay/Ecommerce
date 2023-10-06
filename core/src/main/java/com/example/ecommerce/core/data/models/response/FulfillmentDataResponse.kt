package com.example.ecommerce.core.data.models.response

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class FulfillmentDataResponse(
    val payment: String,
    val invoiceId: String,
    val status: Boolean,
    val date: String,
    val time: String,
    val total: Int,
    val rating : Int
) : Parcelable

fun FulfillmentDetail.fulfillmentToReview(): FulfillmentDataResponse {
    return FulfillmentDataResponse(
        payment = this.payment ?: "",
        invoiceId = this.invoiceId ?: "",
        status = this.status ?: false,
        date = this.date ?: "",
        time = this.time ?: "",
        total = this.total ?: 0,
        rating = this.rating ?: 0
    )
}

fun TransactionDataItem.transactionToReview(): FulfillmentDataResponse {
    return FulfillmentDataResponse(
        payment = this.payment ?: "",
        invoiceId = this.invoiceId ?: "",
        status = this.status ?: false,
        date = this.date ?: "",
        time = this.time ?: "",
        total = this.total ?: 0,
        rating = this.rating ?: 0
    )
}





