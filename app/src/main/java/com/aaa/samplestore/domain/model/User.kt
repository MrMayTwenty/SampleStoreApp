package com.aaa.samplestore.domain.model

import com.aaa.samplestore.data.local.entity.UserEntity

data class User(
    val address: Address,
    val email: String,
    val id: Int,
    val name: Name,
    val password: String,
    val phone: String,
    val username: String
)

data class Address(
    val city: String,
    val geolocation: Geolocation?,
    val number: String,
    val street: String,
    val zipcode: String
)

data class Name(
    val firstname: String,
    val lastname: String
)

data class Geolocation(
    val lat: Double,
    val long: Double
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    email = email,
    passwordHash = password,
    firstName = name.firstname,
    lastName = name.lastname,
    mobileNumber = phone,
    city = address.city,
    street = address.street,
    zipCode = address.zipcode,
    addressNumber = address.number,
)