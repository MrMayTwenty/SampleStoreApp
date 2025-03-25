package com.aaa.samplestore.presentation.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.data.local.sharedpreference.SharedPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val sharedPreferenceManager: SharedPreferenceManager
): ViewModel() {

    private val _selectedLanguage = mutableStateOf(sharedPreferenceManager.getLanguage() ?: "en")
    val selectedLanguage: State<String> = _selectedLanguage

    private val _isDarkMode = mutableStateOf(false)
    val isDarkMode: State<Boolean> = _isDarkMode

    fun getUserProfile(){

    }

    fun getCurrentUserId(): Int?{
        return sessionManager.getUserId()
    }

    fun setLanguage(language: String) {
        _selectedLanguage.value = language
        sharedPreferenceManager.saveLanguage(language)
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun logout() {
        TODO("Not yet implemented")
    }

}