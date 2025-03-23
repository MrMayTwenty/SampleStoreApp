package com.aaa.samplestore.data.remote

import com.aaa.samplestore.data.remote.dto.request.AddUserRequest
import com.aaa.samplestore.data.remote.dto.response.ProductDTO
import com.aaa.samplestore.data.remote.dto.response.UserDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(@Query("page") page: Int): List<ProductDTO>

    @GET("products/{id}")
    suspend fun getProductById(id: Int): ProductDTO

    @GET("products/category")
    suspend fun getProductsByCategory(@Query("type") category: String): List<ProductDTO>

    @POST("users")
    suspend fun createUser(@Body user: AddUserRequest): UserDTO

}