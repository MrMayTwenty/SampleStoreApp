package com.aaa.samplestore.data.remote.dto

data class PayPalCaptureResponse(
    val id: String, // Order ID
    val status: String, // "COMPLETED" if successful
    val payment_source: PaymentSource?,
    val purchase_units: List<PurchaseUnit>?,
    val payer: Payer?,
    val links: List<Link>?
)

data class PaymentSource(
    val paypal: PayPalDetails?
)

data class PayPalDetails(
    val name: PayerName?,
    val email_address: String?,
    val account_id: String?,
    val account_status: String? // "VERIFIED"
)


data class PurchaseUnit(
    val reference_id: String?,
    val amount: Amount?
)

data class Amount(
    val currency_code: String,
    val value: String
)

data class Payer(
    val name: PayerName?,
    val email_address: String?,
    val payer_id: String?
)

data class PayerName(
    val given_name: String?,
    val surname: String?
)

data class Link(
    val href: String,
    val rel: String,
    val method: String
)