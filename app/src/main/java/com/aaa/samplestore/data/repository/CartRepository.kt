package com.aaa.samplestore.data.repository

import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.entity.toCartItem
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.model.toCartItemEntity
import com.aaa.samplestore.domain.repository.ICartRepository
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) : ICartRepository {
    override suspend fun getCartItems(): List<CartItem> {
        return cartDao.getCartItems().map { it.toCartItem() }
    }

    override suspend fun getCartItems(userId: Int): List<CartItem> {
        return cartDao.getCartItems(userId).map { it.toCartItem() }
    }

    override suspend fun removeFromCart(id: Int?) {
        cartDao.removeFromCart(id)
    }

    override suspend fun clearUserCart(userId: Int) {
        cartDao.clearUserCart(userId)
    }

    override suspend fun updateQuantity(id: Int?, quantity: Int) {
        cartDao.updateQuantity(id, quantity)
    }

    override suspend fun addToCart(item: CartItem) {
        cartDao.addToCart(item.toCartItemEntity())
    }

}