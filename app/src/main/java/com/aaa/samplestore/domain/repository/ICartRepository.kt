package com.aaa.samplestore.domain.repository

import com.aaa.samplestore.domain.model.CartItem

interface ICartRepository {
    suspend fun getCartItems(): List<CartItem>
    suspend fun removeFromCart(id: Int?)
    suspend fun updateQuantity(id: Int?, quantity: Int)
    suspend fun addToCart(item: CartItem)
}