package com.aaa.samplestore.presentation.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.usecase.GetCartUseCase
import com.aaa.samplestore.domain.usecase.RemoveFromCartUseCase
import com.aaa.samplestore.domain.usecase.UpdateQuantityUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val updateQuantityUseCase: UpdateQuantityUseCase
) : ViewModel() {

    private val _cartItem = mutableStateOf(ViewModelState<List<CartItem>>())
    val cartItem: State<ViewModelState<List<CartItem>>> = _cartItem

    fun getCartItems() {
        viewModelScope.launch {
            getCartUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Error -> _cartItem.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _cartItem.value = ViewModelState(isLoading = true)
                    is Resource.Success -> _cartItem.value = ViewModelState(data = result.data)
                }
            }
        }
    }

    fun removeFromCart(id: Int) {
        viewModelScope.launch {
            removeFromCartUseCase.invoke(id).collect { result ->
                when (result) {
                    is Resource.Error -> _cartItem.value = _cartItem.value.copy(error = result.message)
                    is Resource.Loading -> _cartItem.value = _cartItem.value.copy(isLoading = true)
                    is Resource.Success -> {
                        _cartItem.value = _cartItem.value.copy(
                            data = _cartItem.value.data?.filterNot { it.cartId == id },
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun updateQuantity(id: Int, newQuantity: Int) {
        viewModelScope.launch {
            updateQuantityUseCase.invoke(id, newQuantity).collect { result ->
                when (result) {
                    is Resource.Error -> _cartItem.value = _cartItem.value.copy(error = result.message)
                    is Resource.Loading -> _cartItem.value = _cartItem.value.copy(isLoading = true)
                    is Resource.Success -> {
                        _cartItem.value = _cartItem.value.copy(
                            data = _cartItem.value.data?.map {
                                if (it.cartId == id) it.copy(quantity = newQuantity) else it
                            },
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}
