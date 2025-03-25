package com.aaa.samplestore.presentation

import kotlinx.serialization.Serializable

sealed class Screen() {
    @Serializable
    object ProductListScreen : Screen()
    @Serializable
    data class ProductDetailScreen(val productId: Int)
    @Serializable
    object CartScreen : Screen()
    @Serializable
    object CheckoutScreen : Screen()
    @Serializable
    object LoginScreen : Screen()
    @Serializable
    object RegisterScreen : Screen()
    @Serializable
    object ProfileScreen : Screen()
    @Serializable
    object WishlistScreen: Screen()
}