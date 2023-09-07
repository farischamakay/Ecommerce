package com.example.ecommerce.data.repository

import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.database.cart.CartDao
import com.example.ecommerce.data.database.wishlist.Wishlist
import com.example.ecommerce.data.database.wishlist.WishlistDao
import javax.inject.Inject

class RoomCartRepository @Inject constructor(
    private val cartDao: CartDao,
    private val wishlistDao: WishlistDao) {

    //ENTITY CART
    fun fetchCartData() = cartDao.getAll()
    suspend fun insertCartData(cart: Cart) = cartDao.insertCart(cart)
    suspend fun updateValues(cartList: List<Cart>) {
        cartDao.update(*cartList.toTypedArray())
    }
    suspend fun deleteById(cart: String) = cartDao.deleteById(cart)
    suspend fun deleteData(vararg cart: Cart) = cartDao.delete(*cart)


    //ENTITY WISHLIST
    fun fetchWishlistData() = wishlistDao.getAll()
    suspend fun insertWishlistData(wishlist: Wishlist) = wishlistDao.insertWishlist(wishlist)
    suspend fun deleteWishlistById(wishlist : String) = wishlistDao.deleteWishlistById(wishlist)

}