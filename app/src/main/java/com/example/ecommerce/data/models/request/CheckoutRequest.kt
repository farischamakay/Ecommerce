package com.example.ecommerce.data.models.request

import android.os.Parcelable
import androidx.annotation.Keep
import com.example.ecommerce.data.database.cart.Cart
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CheckoutRequest(
    val productId: String?,
    val productName: String?,
    val image: String?,
    val brand: String?,
    val description: String?,
    val store: String?,
    val sale: String?,
    val stock: Int?,
    val totalRating: Int?,
    val totalReview: Int?,
    val totalSatisfaction: Int?,
    val productVariant: String,
    val productVariantPrice: Int,
    val isCheck: Boolean = false,
    var quantity: Int = 1,
) : Parcelable

@Keep
@Parcelize
data class ListCheckout(
    val listCheckout: List<CheckoutRequest>
) : Parcelable


fun List<Cart>.toListCheckout(): ListCheckout {
    val data = mutableListOf<CheckoutRequest>()
    this.map {
        data.add(
            CheckoutRequest(
                it.productId,
                it.productName,
                it.image,
                it.brand,
                it.description,
                it.store,
                it.sale,
                it.stock,
                it.totalRating,
                it.totalReview,
                it.totalSatisfaction,
                it.productVariant,
                it.productVariantPrice,
                it.isCheck,
                it.quantity,
            )
        )
    }
    return ListCheckout(data)
}

