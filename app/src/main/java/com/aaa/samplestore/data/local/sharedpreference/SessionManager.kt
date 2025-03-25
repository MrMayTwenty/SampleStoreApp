package com.aaa.samplestore.data.local.sharedpreference

import android.content.Context
import com.aaa.samplestore.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseSharedPreferences(context.getSharedPreferences(Constants.SharedPref.PREF_NAME, Context.MODE_PRIVATE)) {
    fun saveUserId(userId: Int) {
        putInt(Constants.SessionKeys.USER_ID, userId)
    }

    fun getUserId(): Int? {
        val userId = getInt(Constants.SessionKeys.USER_ID, -1)
        return if (userId != -1) userId else null
    }

    fun clearSession() {
        clear()
    }
}