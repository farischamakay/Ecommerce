package com.example.ecommerce.main.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.database.Cart
import com.example.ecommerce.data.repository.RoomCartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val roomCartRepository: RoomCartRepository
) : ViewModel() {
    private val _cartRoom = MutableLiveData<Cart>()
    private val cartRoom : LiveData<Cart> = _cartRoom

    val getDataRoom =
            roomCartRepository.fetchCartData()

    fun updateCheckable(cartList: List<Pair<Cart, Boolean>>){
    viewModelScope.launch {
        val updates = cartList.map { (cartList, isChecked) ->
            cartList.copy(isCheck = isChecked)
        }
        roomCartRepository.updateValues(updates)
        }
    }

    fun deleteCheckedItems(){
        viewModelScope.launch {
            val checkedItems = getDataRoom.value?.filter { it.isCheck } ?: emptyList()
            roomCartRepository.deleteData(*checkedItems.toTypedArray())
        }
    }

    fun selectedAllItems(isChecked : Boolean){
        viewModelScope.launch {
            val cartList = getDataRoom.value ?: emptyList()
            val updateList = cartList.map {it.copy(isCheck = isChecked)}
            roomCartRepository.updateValues(updateList)
        }
    }


}