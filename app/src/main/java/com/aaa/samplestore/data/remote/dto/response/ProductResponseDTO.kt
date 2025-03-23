package com.aaa.samplestore.data.remote.dto.response

data class ProductResponseDTO(
    val message: String,
    val products: List<ProductDTO>,
    val status: String
)