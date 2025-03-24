package com.aaa.samplestore.domain.model

import com.aaa.samplestore.data.local.entity.CartItemEntity

data class CartItem(
    val cartId: Int? = null,
    val productId: Int,
    val title: String,
    val brand: String,
    val image: String,
    val price: Double,
    val quantity: Int = 0
)

fun CartItem.toCartItemEntity(): CartItemEntity {
    return CartItemEntity(
        id = cartId ?: 0,
        productId = productId,
        title = title,
        brand = brand,
        image = image,
        price = price,
        quantity = quantity
    )
}