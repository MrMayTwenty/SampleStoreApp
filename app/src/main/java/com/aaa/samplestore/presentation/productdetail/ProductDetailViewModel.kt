package com.aaa.samplestore.presentation.productdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.domain.usecase.AddToCartUseCase
import com.aaa.samplestore.domain.usecase.GetProductByIdUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    val currentUserId = sessionManager.getUserId()

    private val _productState = mutableStateOf(ViewModelState<Product>())
    val productState: State<ViewModelState<Product>> = _productState

    private val _addToCartState = mutableStateOf(ViewModelState<Unit>())
    val addToCartState: State<ViewModelState<Unit>> = _addToCartState

    fun getProductById(id: Int){
        viewModelScope.launch {
            getProductByIdUseCase(id).collect{ result ->
                when(result){
                    is Resource.Error -> _productState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _productState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> _productState.value = ViewModelState(data = result.data)
                }
            }
        }
    }

    fun addToCart(product: Product, userId: Int? = null, numberOfOrders: Int) {
        viewModelScope.launch {
            addToCartUseCase(
                CartItem(
                    cartId = null,
                    productId = product.id,
                    quantity = numberOfOrders,
                    title = product.title,
                    brand = product.brand,
                    image = product.image,
                    price = product.price,
                    userId = userId
                ),userId,
            ).collect { result ->
                when(result) {
                    is Resource.Error -> _addToCartState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _addToCartState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> _addToCartState.value = ViewModelState(data = result.data)
                }
            }
        }
    }
}
