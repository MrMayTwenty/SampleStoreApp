package com.aaa.samplestore.data.remote.dto.response

import com.aaa.samplestore.domain.model.User
import com.aaa.samplestore.domain.model.Address
import com.aaa.samplestore.domain.model.Name

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

fun UserDTO.toUser(): User = User(
    this.address.toAddress(),
    this.email,
    this.id,
    this.name.toName(),
    this.password,
    this.phone,
    this.username
)

fun Address.toAddress(): Address = Address(
    this.city,
    this.geolocation,
    this.number,
    this.street,
    this.zipcode
)

fun Name.toName(): Name = Name(this.firstname, this.lastname)