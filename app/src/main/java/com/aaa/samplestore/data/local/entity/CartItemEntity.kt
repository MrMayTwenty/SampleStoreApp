package com.aaa.samplestore.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aaa.samplestore.common.RoomConstants
import com.aaa.samplestore.domain.model.CartItem

@Entity(tableName = RoomConstants.Cart.TABLE_NAME,
        foreignKeys = [
            ForeignKey(entity = UserEntity::class,
                parentColumns = [RoomConstants.User.Columns.ID],
                childColumns = [RoomConstants.Cart.Columns.USER_ID],
                onDelete = ForeignKey.CASCADE)
        ])
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = RoomConstants.Cart.Columns.USER_ID) val userId: Int?,
    @ColumnInfo(name = RoomConstants.Cart.Columns.PRODUCT_ID) val productId: Int,
    @ColumnInfo(name = RoomConstants.Cart.Columns.TITLE) val title: String,
    @ColumnInfo(name = RoomConstants.Cart.Columns.BRAND) val brand: String,
    @ColumnInfo(name = RoomConstants.Cart.Columns.IMAGE) val image: String,
    @ColumnInfo(name = RoomConstants.Cart.Columns.PRICE) val price: Double,
    @ColumnInfo(name = RoomConstants.Cart.Columns.QUANTITY) val quantity: Int
)

fun CartItemEntity.toCartItem(): CartItem {
    return CartItem(
        cartId = id,
        userId = userId,
        productId = productId,
        title = title,
        brand = brand,
        image = image,
        price = price,
        quantity = quantity
    )
}