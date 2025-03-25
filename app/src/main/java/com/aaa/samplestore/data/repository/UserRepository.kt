package com.aaa.samplestore.data.repository

import com.aaa.samplestore.data.remote.FakeStoreApi
import com.aaa.samplestore.data.remote.dto.request.AddUserRequest
import com.aaa.samplestore.data.remote.dto.response.UserDTO
import com.aaa.samplestore.data.remote.dto.response.UserResponseDTO
import com.aaa.samplestore.domain.repository.IUserRepository
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val fakeStoreApi: FakeStoreApi
): IUserRepository {
    override suspend fun createUser(request: AddUserRequest): UserResponseDTO {
        return fakeStoreApi.createUser(request)
    }
}