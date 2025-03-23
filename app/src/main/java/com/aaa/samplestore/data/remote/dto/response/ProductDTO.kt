package com.aaa.samplestore.data.remote.dto.response

import com.aaa.samplestore.domain.model.Product

data class ProductDTO(
    val brand: String,
    val category: String,
    val color: String?,
    val description: String,
    val discount: Int?,
    val id: Int,
    val image: String,
    val model: String,
    val onSale: Boolean?,
    val popular: Boolean?,
    val price: Int,
    val title: String
)

fun ProductDTO.toProduct(): Product = Product(
    this.brand,
    this.category,
    this.color,
    this.description,
    this.discount,
    this.id,
    this.image,
    this.model,
    this.onSale,
    this.popular,
    this.price,
    this.title
)