package com.aaa.samplestore.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class PayPalAuthResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int
)