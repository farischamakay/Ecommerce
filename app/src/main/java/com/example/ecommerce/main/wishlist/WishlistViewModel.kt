package com.example.ecommerce.main.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.repository.RoomCartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(private val roomCartRepository: RoomCartRepository)
    : ViewModel() {

    val getDataWishlist =
        roomCartRepository.fetchWishlistData()

    val getDataRoom =
        roomCartRepository.fetchCartData()

    fun deleteItemById(itemId : String){
        viewModelScope.launch {
            roomCartRepository.deleteWishlistById(itemId)
        }
    }
    fun insertToRoom(cart: Cart) {
        viewModelScope.launch {
            roomCartRepository.insertCartData(cart)
        }
    }

    fun updateQuantity(cartList: List<Pair<Cart, Int>>) {
        viewModelScope.launch {
            val updates = cartList.map { (cartList, quantity) ->
                cartList.copy(quantity = quantity)
            }
            roomCartRepository.updateValues(updates)
        }
    }
}