package com.aaa.samplestore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaa.samplestore.data.local.entity.WishListItemEntity

@Dao
interface WishlistDao {

    @Query("UPDATE wishlist SET user_id=:userId WHERE user_id IS NULL")
    suspend fun assignUserToUnownedWishlists(userId: Int)

    @Query("SELECT * FROM wishlist where user_id IS NULL")
    suspend fun getWishlistItems(): List<WishListItemEntity>

    @Query("SELECT * FROM wishlist WHERE product_id=:productId")
    suspend fun getWishlistByProductId(productId:Int): WishListItemEntity

    @Query("SELECT * FROM wishlist WHERE user_id = :userId")
    suspend fun getWishlist(userId: Int): List<WishListItemEntity>

    @Query("DELETE FROM wishlist WHERE id = :id")
    suspend fun removeFromWishlist(id: Int?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishListItemEntity)
}