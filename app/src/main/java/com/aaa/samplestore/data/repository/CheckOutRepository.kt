package com.aaa.samplestore.data.repository

import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.data.remote.PayPalApi
import com.aaa.samplestore.data.remote.dto.PayPalCaptureResponse
import com.aaa.samplestore.data.remote.dto.request.PayPalOrderRequest
import com.aaa.samplestore.data.remote.dto.response.PayPalAuthResponse
import com.aaa.samplestore.data.remote.dto.response.PayPalOrderResponse
import com.aaa.samplestore.domain.repository.ICheckOutRepository
import okhttp3.Credentials
import javax.inject.Inject

class CheckOutRepository @Inject constructor(
    private val payPalApi: PayPalApi
): ICheckOutRepository{
    override suspend fun getAccessToken(): PayPalAuthResponse {
        val authHeader = Credentials.basic(Constants.PAYPAL_CLIENT_ID, Constants.PAYPAL_CLIENT_SECRET)
        return payPalApi.getAccessToken(authHeader)
    }

    override suspend fun createOrderId(authorization: String, orderRequest: PayPalOrderRequest): PayPalOrderResponse {
        return payPalApi.createOrder(authorization, orderRequest = orderRequest)
    }

    override suspend fun captureOrder(
        authorization: String,
        orderId: String
    ): PayPalCaptureResponse {
        return payPalApi.captureOrder(authorization, orderId = orderId)
    }
}