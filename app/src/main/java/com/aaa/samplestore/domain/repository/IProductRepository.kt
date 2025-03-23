package com.aaa.samplestore.domain.repository

import com.aaa.samplestore.data.remote.dto.response.ProductResponseDTO

interface IProductRepository {
    suspend fun getProductsResponse(page: Int): ProductResponseDTO
    suspend fun getProductsByCategory(category: String): ProductResponseDTO
}