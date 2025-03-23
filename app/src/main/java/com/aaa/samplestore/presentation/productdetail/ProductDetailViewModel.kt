package com.aaa.samplestore.presentation.productdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.domain.usecase.GetProductByIdUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModel() {

    private val _productState = mutableStateOf(ViewModelState<Product>())
    val productState: State<ViewModelState<Product>> = _productState

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

    fun addToCart(product: Product, numberOfOrders: Int) {

    }
}
