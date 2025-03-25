package com.aaa.samplestore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaa.samplestore.data.local.entity.CartItemEntity

@Dao
interface CartDao {

    @Query("UPDATE cart SET user_id=:userId WHERE user_id IS NULL")
    suspend fun assignUserToUnownedCarts(userId: Int)

    @Query("SELECT * FROM cart WHERE user_id IS NULL")
    suspend fun getCartItems(): List<CartItemEntity>

    @Query("SELECT * FROM cart WHERE user_id = :userId")
    suspend fun getCartItems(userId: Int): List<CartItemEntity>

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun removeFromCart(id: Int?)

    @Query("DELETE FROM cart WHERE user_id = :userId")
    suspend fun clearUserCart(userId: Int)

    @Query("UPDATE cart SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Int?, quantity: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartItemEntity)


}