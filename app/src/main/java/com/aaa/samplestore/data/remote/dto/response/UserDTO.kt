package com.aaa.samplestore.data.remote.dto.response

data class UserDTO(
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
    val geolocation: Geolocation,
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