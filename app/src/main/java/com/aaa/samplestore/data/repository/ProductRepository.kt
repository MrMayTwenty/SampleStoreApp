package com.aaa.samplestore.data.repository

import com.aaa.samplestore.data.remote.FakeStoreApi
import com.aaa.samplestore.data.remote.dto.response.ProductResponseDTO
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.domain.repository.IProductRepository
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val fakeStoreApi: FakeStoreApi): IProductRepository {
    override suspend fun getProductsResponse(page: Int): ProductResponseDTO {
        return fakeStoreApi.getProducts(page)
    }

    override suspend fun getProductsByCategory(category: String): ProductResponseDTO {
        return fakeStoreApi.getProductsByCategory(category)
    }

}