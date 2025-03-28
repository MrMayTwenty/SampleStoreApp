package com.aaa.samplestore.data.remote

import com.aaa.samplestore.data.remote.dto.PayPalCaptureResponse
import com.aaa.samplestore.data.remote.dto.request.PayPalOrderRequest
import com.aaa.samplestore.data.remote.dto.response.PayPalAuthResponse
import com.aaa.samplestore.data.remote.dto.response.PayPalOrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PayPalApi {
    @FormUrlEncoded
    @POST("v1/oauth2/token")
    suspend fun getAccessToken(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded",
        @Field("grant_type") grantType: String = "client_credentials"
    ): PayPalAuthResponse

    @POST("v2/checkout/orders")
    suspend fun createOrder(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body orderRequest: PayPalOrderRequest
    ): PayPalOrderResponse

    @POST("v2/checkout/orders/{orderId}/capture")
    suspend fun captureOrder(
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Path("orderId") orderId: String
    ): PayPalCaptureResponse

}