package com.aaa.samplestore.domain.model

import com.aaa.samplestore.data.local.entity.WishListItemEntity

data class WishlistItem(
    val wishlistItemId: Int? = null,
    val userId: Int?,
    val productId: Int,
    val title: String,
    val brand: String,
    val image: String,
    val price: Double
)

fun WishlistItem.toEntity(): WishListItemEntity = WishListItemEntity(
    id = wishlistItemId ?: 0,
    userId = userId,
    productId = productId,
    title = title,
    brand = brand,
    image = image,
    price = price
)