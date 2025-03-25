package com.aaa.samplestore.domain.repository

import com.aaa.samplestore.data.remote.dto.request.AddUserRequest
import com.aaa.samplestore.data.remote.dto.response.UserDTO
import com.aaa.samplestore.data.remote.dto.response.UserResponseDTO

interface IUserRepository {
    suspend fun createUser(request: AddUserRequest): UserResponseDTO
}