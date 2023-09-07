package com.example.ecommerce.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CartDao {
    @Query("Select * from cart")
    fun getAll(): LiveData<List<Cart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: Cart)

    @Query("DELETE FROM cart where productId = :itemId")
    suspend fun deleteById(itemId : String)

    @Update
    suspend fun update(vararg cart: Cart)

    @Delete
    suspend fun delete(vararg cart: Cart)
}