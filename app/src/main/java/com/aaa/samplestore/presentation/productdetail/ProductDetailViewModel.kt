package com.aaa.samplestore.presentation.productdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.domain.model.WishlistItem
import com.aaa.samplestore.domain.usecase.AddProductToWishlistUseCase
import com.aaa.samplestore.domain.usecase.AddToCartUseCase
import com.aaa.samplestore.domain.usecase.GetProductByIdUseCase
import com.aaa.samplestore.domain.usecase.GetWishlistByProductIdUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val sessionManager: SessionManager,
    private val getWishlistByProductIdUseCase: GetWishlistByProductIdUseCase,
    private val addProductToWishlistUseCase: AddProductToWishlistUseCase,
) : ViewModel() {

    val currentUserId = sessionManager.getUserId()

    private val _productState = mutableStateOf(ViewModelState<Product>())
    val productState: State<ViewModelState<Product>> = _productState

    private val _addToCartState = mutableStateOf(ViewModelState<Unit>())
    val addToCartState: State<ViewModelState<Unit>> = _addToCartState

    private val _productLikeState = mutableStateOf(ViewModelState<Boolean>())
    val productLikeState: State<ViewModelState<Boolean>> = _productLikeState

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

    fun getIsProductOnWishlist(id: Int){
        viewModelScope.launch {
            getWishlistByProductIdUseCase.invoke(id).collect { result ->
                when(result){
                    is Resource.Error -> _productLikeState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _productLikeState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> {
                        val isSuccess = result.data != null
                        _productLikeState.value = ViewModelState(data = isSuccess)
                    }
                }
            }
        }
    }

    fun addToCart(product: Product, userId: Long? = null, numberOfOrders: Int) {
        viewModelScope.launch {
            addToCartUseCase.invoke(
                CartItem(
                    cartId = null,
                    productId = product.id,
                    quantity = numberOfOrders,
                    title = product.title,
                    brand = product.brand,
                    image = product.image,
                    price = product.price,
                    userId = userId?.toInt()
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

    fun addToWishlist(product: Product, userId: Long?) {
        viewModelScope.launch {
            addProductToWishlistUseCase.invoke(
                WishlistItem(
                    wishlistItemId = null,
                    userId = userId?.toInt(),
                    productId = product.id,
                    title = product.title,
                    brand = product.brand,
                    image = product.image,
                    price = product.price
                ),
                userId = userId,
            ).collect { result ->
                when(result){
                    is Resource.Error -> _productLikeState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _productLikeState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> {
                        val isSuccess = result.data != null
                        _productLikeState.value = ViewModelState(data = isSuccess)
                    }
                }
            }
        }
    }
}
