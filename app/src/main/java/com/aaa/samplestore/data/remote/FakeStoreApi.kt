package com.aaa.samplestore.data.remote

import com.aaa.samplestore.data.remote.dto.request.AddUserRequest
import com.aaa.samplestore.data.remote.dto.response.ProductDTO
import com.aaa.samplestore.data.remote.dto.response.ProductListResponseDTO
import com.aaa.samplestore.data.remote.dto.response.ProductResponseDTO
import com.aaa.samplestore.data.remote.dto.response.UserDTO
import com.aaa.samplestore.data.remote.dto.response.UserResponseDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(@Query("page") page: Int): ProductListResponseDTO

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductResponseDTO

    @GET("products/category")
    suspend fun getProductsByCategory(@Query("type") category: String): ProductListResponseDTO

    @POST("users")
    suspend fun createUser(@Body user: AddUserRequest): UserResponseDTO

}