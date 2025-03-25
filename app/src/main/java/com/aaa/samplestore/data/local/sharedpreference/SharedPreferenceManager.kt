package com.aaa.samplestore.data.local.sharedpreference

import android.content.Context
import com.aaa.samplestore.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(@ApplicationContext context: Context): BaseSharedPreferences(context.getSharedPreferences(Constants.SharedPref.PREF_NAME, Context.MODE_PRIVATE)) {
    fun saveLanguage(language: String) {
        putString(Constants.SharedPref.LANGUAGE, language)
    }

    fun getLanguage(): String? {
        return getString(Constants.SharedPref.LANGUAGE)
    }
}