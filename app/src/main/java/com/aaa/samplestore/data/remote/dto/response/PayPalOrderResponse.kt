package com.aaa.samplestore.data.remote.dto.response

import com.aaa.samplestore.domain.model.PayPalOrderStatus
import com.google.gson.annotations.SerializedName

data class PayPalOrderResponse(
    @SerializedName("id") val orderId: String,
    @SerializedName("status") val status: String
)

fun PayPalOrderResponse.toPayPalOrderStatus(): PayPalOrderStatus = PayPalOrderStatus(
    orderId = orderId,
    status = status
)