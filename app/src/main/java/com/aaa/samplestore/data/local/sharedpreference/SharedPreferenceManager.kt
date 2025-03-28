package com.aaa.samplestore.data.local.sharedpreference

import android.content.Context
import com.aaa.samplestore.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(@ApplicationContext context: Context): BaseSharedPreferences(context.getSharedPreferences(Constants.SharedPref.PREF_NAME, Context.MODE_PRIVATE)) {
    fun savePurchaseSuccessState(state: Boolean) {
        putBoolean(Constants.SharedPref.PURCHASE_SUCCESS, state)
    }

    fun getPurchaseSuccessState(): Boolean {
        return getBoolean(Constants.SharedPref.PURCHASE_SUCCESS, false)
    }
}