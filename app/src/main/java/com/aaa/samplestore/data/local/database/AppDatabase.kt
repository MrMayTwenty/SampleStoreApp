package com.aaa.samplestore.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.entity.CartItemEntity

@Database(entities = [CartItemEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(context, AppDatabase::class.java, "sample_store.db")
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}