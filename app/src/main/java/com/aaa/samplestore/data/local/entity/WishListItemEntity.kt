package com.aaa.samplestore.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aaa.samplestore.common.RoomConstants
import com.aaa.samplestore.domain.model.WishlistItem

@Entity(tableName = RoomConstants.Wishlist.TABLE_NAME,
    foreignKeys = [
        ForeignKey(entity = UserEntity::class,
            parentColumns = [RoomConstants.User.Columns.ID],
            childColumns = [RoomConstants.Cart.Columns.USER_ID],
            onDelete = ForeignKey.CASCADE)
    ])
data class WishListItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = RoomConstants.Wishlist.Columns.USER_ID) val userId: Int?,
    @ColumnInfo(name = RoomConstants.Wishlist.Columns.PRODUCT_ID) val productId: Int,
    @ColumnInfo(name = RoomConstants.Wishlist.Columns.TITLE) val title: String,
    @ColumnInfo(name = RoomConstants.Wishlist.Columns.BRAND) val brand: String,
    @ColumnInfo(name = RoomConstants.Wishlist.Columns.IMAGE) val image: String,
    @ColumnInfo(name = RoomConstants.Wishlist.Columns.PRICE) val price: Double,
)


fun WishListItemEntity.toWishlistItem(): WishlistItem {
    return WishlistItem(
        wishlistItemId = id,
        userId = userId,
        productId = productId,
        title = title,
        brand = brand,
        image = image,
        price = price,
    )
}