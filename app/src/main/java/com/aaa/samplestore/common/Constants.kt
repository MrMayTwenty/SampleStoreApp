package com.aaa.samplestore.common

import com.aaa.samplestore.BuildConfig

object Constants {
    const val BASE_URL = BuildConfig.BASE_URL
    const val PAYPAL_BASE_URL = BuildConfig.PAYPAL_BASE_URL
    const val PAYPAL_CLIENT_ID = BuildConfig.PAYPAL_CLIENT_ID
    const val PAYPAL_CLIENT_SECRET = BuildConfig.PAYPAL_CLIENT_SECRET

    const val TEST_CARD_NUMBER = "4032030060604767"
    const val TEST_CARD_CVV = "679"
    const val TEST_CARD_EXPIRY_MONTH = "07"
    const val TEST_CARD_EXPIRY_YEAR = "2030"

    object SharedPref {
        const val ENCRYPTED_SHARED_PREF_NAME = "encrypted_shared_pref"
        const val PREF_NAME = "sample_store_pref"
        const val PAYPAL_OAUTH_ACCESS_TOKEN = "PAYPAL_OAUTH_ACCESS_TOKEN"
        const val PURCHASE_SUCCESS = "PURCHASE_SUCCESS"
    }

    object SessionKeys {
        const val USER_ID = "USER_ID"
        const val USER_NAME = "USER_NAME"
        const val USER_ADDRESS = "USER_ADDRESS"
        const val USER_PHONE = "USER_PHONE"
    }

    enum class ProductFilter(val value: String) {
        ALL("all"),
        POPULAR("popular"),
        SALE("sale");
    }

    enum class ProductCategory(val value: String) {
        ALL(""),
        TV("tv"),
        AUDIO("audio"),
        LAPTOP("laptop"),
        MOBILE("mobile"),
        GAMING("gaming"),
        APPLIANCES("appliances");
    }

}