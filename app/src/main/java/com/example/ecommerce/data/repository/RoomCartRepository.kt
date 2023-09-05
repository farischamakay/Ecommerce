package com.example.ecommerce.data.repository

import com.example.ecommerce.data.database.Cart
import com.example.ecommerce.data.database.CartDao
import javax.inject.Inject

class RoomCartRepository @Inject constructor(private val cartDao: CartDao) {

    suspend fun insertCartData(cart: Cart) = cartDao.insertCart(cart)

    fun fetchCartData() = cartDao.getAll()

    suspend fun updateValues(cartList: List<Cart>) {
        cartDao.update(*cartList.toTypedArray())
    }

    suspend fun deleteData(vararg cart: Cart) = cartDao.delete(*cart)
}