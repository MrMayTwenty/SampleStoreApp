package com.aaa.samplestore.data.local.sharedpreference

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.aaa.samplestore.common.Constants
import javax.inject.Inject

class EncryptedSharedPreferenceManager @Inject constructor(context: Context) : BaseSharedPreferences(
    EncryptedSharedPreferences.create(
        context,
        Constants.SharedPref.ENCRYPTED_SHARED_PREF_NAME,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )) {
    fun saveOauthAccessToken(value: String){
        putString(Constants.SharedPref.PAYPAL_OAUTH_ACCESS_TOKEN,value)
    }

    fun getOAuthAccessToken(): String{
        return getString(Constants.SharedPref.PAYPAL_OAUTH_ACCESS_TOKEN) ?: ""
    }
}