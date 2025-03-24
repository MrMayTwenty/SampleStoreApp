package com.aaa.samplestore.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aaa.samplestore.domain.model.CartItem

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "quantity") val quantity: Int
)


fun CartItemEntity.toCartItem(): CartItem {
    return CartItem(
        cartId = id,
        productId = productId,
        title = title,
        brand = brand,
        image = image,
        price = price,
        quantity = quantity
    )
}