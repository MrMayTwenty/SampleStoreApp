package com.aaa.samplestore.common

import com.aaa.samplestore.BuildConfig

object Constants {
    const val BASE_URL = BuildConfig.BASE_URL

    object SharedPref {
        const val PREF_NAME = "sample_store_pref"
        const val LANGUAGE = "language"
    }

    object SessionKeys {
        const val USER_ID = "user_id"
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