package com.aaa.samplestore.data.local.sharedpreference

import android.content.SharedPreferences

abstract class BaseSharedPreferences(private val sharedPreferences: SharedPreferences) {
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    protected fun putString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    protected fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    protected fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    protected fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    protected fun putInt(key: String, value: Int) {
        editor.putInt(key, value).apply()
    }

    protected fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    protected fun putFloat(key: String, value: Float) {
        editor.putFloat(key, value).apply()
    }

    protected fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    protected fun putLong(key: String, value: Long) {
        editor.putLong(key, value).apply()
    }

    protected fun getLong(key: String, defaultValue: Long = 0L): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    protected fun remove(key: String) {
        editor.remove(key).apply()
    }

    fun clear() {
        editor.clear().apply()
    }

    protected fun setStringSet(key: String, value: Set<String>) {
        editor.putStringSet(key, value).apply()
    }

    protected fun getStringSet(key: String, defaultValue: Set<String>? = null): Set<String>? {
        return sharedPreferences.getStringSet(key, defaultValue)
    }
}