package com.aaa.samplestore.presentation.card

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.paypal.android.cardpayments.Card
import com.paypal.android.cardpayments.CardApproveOrderResult
import com.paypal.android.cardpayments.CardAuthChallenge
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.cardpayments.CardPresentAuthChallengeResult
import com.paypal.android.cardpayments.CardRequest
import com.paypal.android.cardpayments.threedsecure.SCA
import com.paypal.android.corepayments.Address
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionManager: SessionManager
) : ViewModel() {

    private var orderId: String = ""
    private val paypalConfig = CoreConfig(Constants.PAYPAL_CLIENT_ID, environment = Environment.SANDBOX)
    private val cardClient = CardClient(context, paypalConfig)

    private val _cardNumber = mutableStateOf(Constants.TEST_CARD_NUMBER)
    val cardNumber: State<String> = _cardNumber

    private val _expirationMonth = mutableStateOf(Constants.TEST_CARD_EXPIRY_MONTH)
    val expirationMonth: State<String> = _expirationMonth

    private val _expirationYear = mutableStateOf(Constants.TEST_CARD_EXPIRY_YEAR)
    val expirationYear: State<String> = _expirationYear

    private val _securityCode = mutableStateOf(Constants.TEST_CARD_CVV)
    val securityCode: State<String> = _securityCode

    private val _cardRequest = mutableStateOf<CardRequest?>(null)
    val cardRequest: State<CardRequest?> = _cardRequest

    fun submitCardDetails() {
        val card = Card(
            number = _cardNumber.value,
            expirationMonth = _expirationMonth.value,
            expirationYear = _expirationYear.value,
            securityCode = _securityCode.value,
            billingAddress = Address(
                streetAddress = sessionManager.getUserAddress(),
                extendedAddress = "Apt. 1A",
                locality = "Anytown",
                region = "CA",
                postalCode = "12345",
                countryCode = "US"
            )
        )

        _cardRequest.value = CardRequest(
            orderId = orderId,
            card = card,
            returnUrl = "com.aaa.samplestore://return_url",
            sca = SCA.SCA_ALWAYS,
        )
    }

    fun updateCardNumber(cardNumber: String) {
        _cardNumber.value = cardNumber
    }

    fun updateExpirationMonth(expirationMonth: String) {
        _expirationMonth.value = expirationMonth
    }

    fun updateExpirationYear(expirationYear: String) {
        _expirationYear.value = expirationYear
    }

    fun updateSecurityCode(securityCode: String) {
        _securityCode.value = securityCode
    }

    fun resetTestCard() {
        _cardNumber.value = Constants.TEST_CARD_NUMBER
        _expirationMonth.value = Constants.TEST_CARD_EXPIRY_MONTH
        _expirationYear.value = Constants.TEST_CARD_EXPIRY_YEAR
        _securityCode.value = Constants.TEST_CARD_CVV
    }


    fun setOrderId(orderId: String) {
        this.orderId = orderId
    }

}