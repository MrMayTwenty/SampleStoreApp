package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.domain.model.PayPalOrderStatus
import com.paypal.android.cardpayments.Card
import com.paypal.android.cardpayments.CardApproveOrderResult
import com.paypal.android.cardpayments.CardApproveOrderResult.Success
import com.paypal.android.cardpayments.CardClient
import com.paypal.android.cardpayments.CardRequest
import com.paypal.android.cardpayments.threedsecure.SCA
import com.paypal.android.corepayments.Address
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckOutOrderUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(orderStatus: PayPalOrderStatus):Flow<Resource<Unit>> = flow{
        try {
            val config = CoreConfig(Constants.PAYPAL_CLIENT_ID, environment = Environment.SANDBOX)
            val cardClient = CardClient(context,config)
            val card = Card(
                number = "4005519200000004",
                expirationMonth = "01",
                expirationYear = "2025",
                securityCode = "123",
                billingAddress = Address(
                    streetAddress = "123 Main St.",
                    extendedAddress = "Apt. 1A",
                    locality = "Anytown",
                    region = "CA",
                    postalCode = "12345",
                    countryCode = "US"
                )
            )
            val cardRequest = CardRequest(
                orderId = orderStatus.orderId,
                card = card,
                returnUrl = "com.aaa.samplestore://return_url",
                sca = SCA.SCA_ALWAYS,
            )
            cardClient.approveOrder(cardRequest) {
                callback ->
                when(callback){
                    is CardApproveOrderResult.AuthorizationRequired -> TODO()
                    is CardApproveOrderResult.Failure -> TODO()
                    is Success -> TODO()
                }
            }
        } catch (e: Exception){

        }
    }
}
