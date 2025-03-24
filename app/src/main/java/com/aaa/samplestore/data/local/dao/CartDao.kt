package com.aaa.samplestore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaa.samplestore.data.local.entity.CartItemEntity

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    suspend fun getCartItems(): List<CartItemEntity>

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun removeFromCart(id: Int?)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Int?, quantity: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartItemEntity)
}