package com.aaa.samplestore.domain.model

data class Checkout(
    val totalPrice: Double? = 0.0,
    val name: String = "",
    val address: String = "",
    val contactNumber: String = "",
    val paymentMethod: String = "",
    val isLoading: Boolean = false,
    val invoiceUrl: String? = null
)
