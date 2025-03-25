package com.aaa.samplestore.presentation.productlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.domain.usecase.AddToCartUseCase
import com.aaa.samplestore.domain.usecase.GetAllProductsUseCase
import com.aaa.samplestore.domain.usecase.GetProductByCategoryUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.annotation.meta.When
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val getProductByCategoryUseCase: GetProductByCategoryUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val sessionManager: SessionManager

) : ViewModel() {

    val originalProducts = mutableStateOf<List<Product>>(emptyList())

    val _productsState = mutableStateOf<ViewModelState<List<Product>>>(ViewModelState())
    val productsState: State<ViewModelState<List<Product>>> = _productsState

    val _selectedFilter = mutableStateOf(Constants.ProductFilter.ALL)
    val selectedFilter: State<Constants.ProductFilter> = _selectedFilter

    val _selectedCategory = mutableStateOf(Constants.ProductCategory.ALL)
    val selectedCategory: State<Constants.ProductCategory> = _selectedCategory

    private val _addToCartState = mutableStateOf(ViewModelState<Unit>())
    val addToCartState: State<ViewModelState<Unit>> = _addToCartState

    fun getCurrentUserId():Long?{
        return sessionManager.getUserId()
    }

    fun getAllProducts(page: Int) {
        viewModelScope.launch {
            getAllProductsUseCase(page).collect { result ->
                when(result) {
                    is Resource.Error -> _productsState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _productsState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> {
                        originalProducts.value = result.data ?: emptyList()
                        _productsState.value = ViewModelState(data = result.data)
                    }
                }
            }
        }
    }

    fun showAllProducts() {
        _selectedFilter.value = Constants.ProductFilter.ALL
        _productsState.value = ViewModelState(data = originalProducts.value)
    }

    fun showPopularProducts() {
        _selectedFilter.value = Constants.ProductFilter.POPULAR
        _productsState.value = ViewModelState(data = originalProducts.value.filter { it.popular == true })
    }

    fun showSaleProducts() {
        _selectedFilter.value = Constants.ProductFilter.SALE
        _productsState.value = ViewModelState(data = originalProducts.value.filter { it.onSale == true })
    }

    fun showProductsByCategory(category: Constants.ProductCategory) {
        _selectedCategory.value = category
        if(category == Constants.ProductCategory.ALL) {
            _productsState.value = ViewModelState(data = originalProducts.value)
            return
        }

        viewModelScope.launch {
            getProductByCategoryUseCase(category.value).collect { result ->
                when(result) {
                    is Resource.Error -> _productsState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _productsState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> _productsState.value = ViewModelState(data = result.data)
                }
            }
        }
    }

    fun addToCart(product: Product, userId: Long? = null){
        viewModelScope.launch {
            addToCartUseCase.invoke(CartItem(
                cartId = null,
                productId = product.id,
                quantity = 1,
                title = product.title,
                brand = product.brand,
                image = product.image,
                price = product.price,
                userId = userId?.toInt()
            ),userId,).collect { result ->
                when(result) {
                    is Resource.Error -> _addToCartState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _addToCartState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> _addToCartState.value = ViewModelState(data = result.data)
                }
            }
        }
    }
}
