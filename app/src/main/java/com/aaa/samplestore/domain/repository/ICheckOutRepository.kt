package com.aaa.samplestore.domain.repository

import com.aaa.samplestore.data.remote.dto.request.PayPalOrderRequest
import com.aaa.samplestore.data.remote.dto.response.PayPalAuthResponse
import com.aaa.samplestore.data.remote.dto.response.PayPalOrderResponse
import retrofit2.Response

interface ICheckOutRepository {
    suspend fun getAccessToken(): PayPalAuthResponse
    suspend fun createOrderId(authorization: String, orderRequest: PayPalOrderRequest): PayPalOrderResponse
}