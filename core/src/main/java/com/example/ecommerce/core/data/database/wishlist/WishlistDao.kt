package com.example.ecommerce.core.data.database.wishlist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WishlistDao {

    @Query("Select * from wishlist")
    fun getAll(): LiveData<List<Wishlist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(wishlist: Wishlist)

    @Query("DELETE FROM wishlist where productId = :itemId")
    suspend fun deleteWishlistById(itemId: String)

    @Update
    suspend fun updateWishlist(vararg wishlist: Wishlist)

}