package com.aaa.samplestore.presentation.checkout

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.model.Checkout
import com.aaa.samplestore.domain.model.PayPalOrderStatus
import com.aaa.samplestore.domain.usecase.GetPayPalOrderIdUseCase
import com.aaa.samplestore.domain.usecase.GetCartUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val sessionManager: SessionManager,
    private val getPayPalOrderIdUseCase: GetPayPalOrderIdUseCase,
) : ViewModel() {

    private val _checkoutFormState = mutableStateOf(Checkout(
        name = sessionManager.getUserName() ?: "",
        address = sessionManager.getUserAddress() ?: "",
        contactNumber = sessionManager.getUserPhone() ?: ""))
    val checkoutFormState: State<Checkout> = _checkoutFormState

    private val _payPalOrderIdState = mutableStateOf(ViewModelState<PayPalOrderStatus>())
    val payPalOrderIdState: State<ViewModelState<PayPalOrderStatus>> = _payPalOrderIdState

    private val _cartState = mutableStateOf(ViewModelState<List<CartItem>>())
    val cartState: State<ViewModelState<List<CartItem>>> = _cartState

    fun onNameChange(newName: String) {
        _checkoutFormState.value = _checkoutFormState.value.copy(name = newName)
    }

    fun onAddressChange(newAddress: String) {
        _checkoutFormState.value = _checkoutFormState.value.copy(address = newAddress)
    }

    fun onContactChange(newContact: String) {
        _checkoutFormState.value = _checkoutFormState.value.copy(contactNumber = newContact)
    }

    fun loadCartItems() {
        val userId = sessionManager.getUserId()
        viewModelScope.launch {
            getCartUseCase.invoke(userId).collect { result ->
                when (result) {
                    is Resource.Error ->  _cartState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _cartState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> {
                        _cartState.value = ViewModelState(data = result.data)
                        _checkoutFormState.value = _checkoutFormState.value.copy(totalPrice = result.data?.sumOf { it.price * it.quantity } ?: 0.0)
                    }
                }
            }
        }
    }

    fun processPayPalPayment(){
        viewModelScope.launch {
            getPayPalOrderIdUseCase.invoke().collect { result ->
                when(result){
                    is Resource.Error -> _payPalOrderIdState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _payPalOrderIdState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> _payPalOrderIdState.value = ViewModelState(data = result.data)
                }
            }
        }
    }
}

