package com.aaa.samplestore.data.remote.dto.request

import com.aaa.samplestore.data.local.entity.UserEntity
import com.aaa.samplestore.domain.model.User

data class AddUserRequest(
    val address: Address,
    val email: String,
    val name: Name,
    val password: String,
    val phone: String,
    val username: String
)

data class Address(
    val city: String,
    val number: String,
    val street: String,
    val zipcode: String
)

data class Name(
    val firstname: String,
    val lastname: String
)

fun AddUserRequest.toEntity(): UserEntity = UserEntity(
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

fun AddUserRequest.toUser(): User = User(
    address = com.aaa.samplestore.domain.model.Address(
        city = address.city,
        geolocation = null,
        number = address.number,
        street = address.street,
        zipcode = address.zipcode
    ),
    email = email,
    id = 0,
    name = com.aaa.samplestore.domain.model.Name(
        firstname = name.firstname,
        lastname = name.lastname
    ),
    password = password,
    phone = phone,
    username = username
)