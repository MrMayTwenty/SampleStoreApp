package com.aaa.samplestore.data.local.sharedpreference

import android.content.Context
import com.aaa.samplestore.common.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseSharedPreferences(context.getSharedPreferences(Constants.SharedPref.PREF_NAME, Context.MODE_PRIVATE)) {
    fun saveUserId(userId: Long) {
        putLong(Constants.SessionKeys.USER_ID, userId)
    }

    fun getUserId(): Long? {
        val userId = getLong(Constants.SessionKeys.USER_ID, -1L)
        return if (userId != -1L) userId else null
    }

    fun clearSession() {
        clear()
    }
}