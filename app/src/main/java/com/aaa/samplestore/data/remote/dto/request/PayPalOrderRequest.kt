package com.aaa.samplestore.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class PayPalOrderRequest(
    @SerializedName("intent")
    val intent: String,
    @SerializedName("purchase_units") val
    purchaseUnits: List<PurchaseUnit>
)

data class PurchaseUnit(
    @SerializedName("reference_id")
    val referenceId: String,
    @SerializedName("amount")
    val amount: Amount
)

data class Amount(
    @SerializedName("currency_code")
    val currencyCode: String,
    @SerializedName("value")
    val value: String
)