package com.aaa.samplestore.data.remote.dto.response

import com.aaa.samplestore.domain.model.Product

data class ProductResponseDTO(
    val message: String,
    val products: List<ProductDTO>,
    val status: String
)

fun ProductResponseDTO.toProductList(): List<Product>{
    val products = this.products.map { it.toProduct() }
    return products
}