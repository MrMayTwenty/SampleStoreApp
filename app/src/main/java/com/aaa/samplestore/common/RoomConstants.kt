package com.aaa.samplestore.common

object RoomConstants {
    const val DATABASE_NAME = "sample_store_db"
        object User {
            const val TABLE_NAME = "users"
            object Columns {
                const val ID = "id"
                const val PASSWORD_HASHED = "password_hashed"
                const val FIRSTNAME = "firstname"
                const val LASTNAME = "lastname"
                const val EMAIL = "email"
                const val MOBILE_NUMBER = "mobile_number"
                const val CITY = "city"
                const val STREET = "street"
                const val ZIPCODE = "zipcode"
                const val ADDRESS_NUMBER = "address_number"
            }
        }

        object Cart {
            const val TABLE_NAME = "cart"
            object Columns {
                const val ID = "id"
                const val USER_ID = "user_id"
                const val PRODUCT_ID = "product_id"
                const val TITLE = "title"
                const val BRAND = "brand"
                const val IMAGE = "image"
                const val PRICE = "price"
                const val QUANTITY = "quantity"
            }
        }

    object Wishlist {
        const val TABLE_NAME = "wishlist"
        object Columns {
            const val ID = "id"
            const val USER_ID = "user_id"
            const val PRODUCT_ID = "product_id"
            const val TITLE = "title"
            const val BRAND = "brand"
            const val IMAGE = "image"
            const val PRICE = "price"
        }
    }

}