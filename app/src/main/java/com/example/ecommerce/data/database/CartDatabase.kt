package com.example.ecommerce.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Cart::class], version = 1, exportSchema = false)
abstract class CartDatabase : RoomDatabase(){
    abstract fun cartDao() : CartDao

}