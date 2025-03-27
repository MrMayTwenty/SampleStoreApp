package com.aaa.samplestore.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.common.RoomConstants
import com.aaa.samplestore.domain.model.Address
import com.aaa.samplestore.domain.model.Name
import com.aaa.samplestore.domain.model.User

@Entity(tableName = RoomConstants.User.TABLE_NAME)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = RoomConstants.User.Columns.EMAIL)val email: String,
    @ColumnInfo(name = RoomConstants.User.Columns.PASSWORD_HASHED)val passwordHash: String, // Store the hashed password
    @ColumnInfo(name = RoomConstants.User.Columns.FIRSTNAME)val firstName: String,
    @ColumnInfo(name = RoomConstants.User.Columns.LASTNAME)val lastName: String,
    @ColumnInfo(name = RoomConstants.User.Columns.MOBILE_NUMBER)val mobileNumber: String,
    @ColumnInfo(name = RoomConstants.User.Columns.CITY)val city: String,
    @ColumnInfo(name = RoomConstants.User.Columns.STREET)val street: String,
    @ColumnInfo(name = RoomConstants.User.Columns.ZIPCODE)val zipCode: String,
    @ColumnInfo(name = RoomConstants.User.Columns.ADDRESS_NUMBER)val addressNumber: String

)

fun UserEntity.toUser(): User = User(
    address = Address(
        city = city,
        geolocation = null,
        number = addressNumber,
        street = street,
        zipcode = zipCode
    ),
    email = email,
    id = id,
    name = Name(
        firstname = firstName,
        lastname = lastName
    ),
    password = passwordHash,
    phone = mobileNumber,
    username = email
)