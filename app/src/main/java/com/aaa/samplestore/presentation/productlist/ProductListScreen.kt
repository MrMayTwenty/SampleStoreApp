package com.aaa.samplestore.presentation.productlist

import androidx.compose.runtime.Composable

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onProductClick: (productId: Int) -> Unit,
    onCartClick: () -> Unit,
    onCheckoutClick: () -> Unit
) {
}