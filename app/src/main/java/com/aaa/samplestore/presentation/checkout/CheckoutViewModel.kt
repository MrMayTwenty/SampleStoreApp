package com.aaa.samplestore.presentation.checkout

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.model.Checkout
import com.aaa.samplestore.domain.usecase.GetCartUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase
) : ViewModel() {

    private val _checkoutState = mutableStateOf(Checkout())
    val checkoutState: State<Checkout> = _checkoutState

    private val _cartState = mutableStateOf(ViewModelState<List<CartItem>>())
    val cartState: State<ViewModelState<List<CartItem>>> = _cartState

    fun onNameChange(newName: String) {
        _checkoutState.value = _checkoutState.value.copy(name = newName)
    }

    fun onAddressChange(newAddress: String) {
        _checkoutState.value = _checkoutState.value.copy(address = newAddress)
    }

    fun onContactChange(newContact: String) {
        _checkoutState.value = _checkoutState.value.copy(contactNumber = newContact)
    }

    fun onPaymentMethodChange(method: String) {
        _checkoutState.value = _checkoutState.value.copy(paymentMethod = method)
    }

    fun generateInvoice() {
        viewModelScope.launch {
//            _checkoutState.value = _checkoutState.value.copy(isLoading = true)
//            val result = invoiceUseCase.createInvoice(
//                name = _checkoutState.value.name,
//                address = _checkoutState.value.address,
//                contact = _checkoutState.value.contactNumber
//            )
//            _checkoutState.value = _checkoutState.value.copy(isLoading = false, invoiceUrl = result)
        }
    }

    fun processGooglePay(onSuccess: () -> Unit) {
        viewModelScope.launch {
//            _checkoutState.value = _checkoutState.value.copy(isLoading = true)
//            val result = googlePayUseCase.processPayment()
//            if (result.isSuccess) {
//                onSuccess()
//            }
//            _checkoutState.value = _checkoutState.value.copy(isLoading = false)
        }
    }

    fun loadCartItems() {
        viewModelScope.launch {
            getCartUseCase.invoke().collect { result ->
                when (result) {
                    is Resource.Error ->  _cartState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _cartState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> {
                        _cartState.value = ViewModelState(data = result.data)
                        _checkoutState.value = _checkoutState.value.copy(totalPrice = result.data?.sumOf { it.price * it.quantity } ?: 0.0)
                    }
                }
            }
        }
    }
}

