package com.aaa.samplestore.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aaa.samplestore.common.RoomConstants
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.dao.UserDao
import com.aaa.samplestore.data.local.dao.WishlistDao
import com.aaa.samplestore.data.local.entity.CartItemEntity
import com.aaa.samplestore.data.local.entity.UserEntity
import com.aaa.samplestore.data.local.entity.WishListItemEntity

@Database(entities = [
    CartItemEntity::class,
    UserEntity::class,
    WishListItemEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun userDao(): UserDao
    abstract fun wishlistDao(): WishlistDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, AppDatabase::class.java, RoomConstants.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}