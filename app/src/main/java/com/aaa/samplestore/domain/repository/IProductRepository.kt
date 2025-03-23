package com.aaa.samplestore.domain.repository

import com.aaa.samplestore.data.remote.dto.response.ProductListResponseDTO
import com.aaa.samplestore.data.remote.dto.response.ProductResponseDTO

interface IProductRepository {
    suspend fun getProductsResponse(page: Int): ProductListResponseDTO
    suspend fun getProductsByCategory(category: String): ProductListResponseDTO
    suspend fun getProductById(id: Int): ProductResponseDTO
}