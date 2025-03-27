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

    private val _userName = mutableStateOf("")
    val userNameState: State<String> = _userName
    private val _userAddress = mutableStateOf("")
    val userAddressState: State<String> = _userAddress
    private val _userPhone = mutableStateOf("")
    val userPhoneState: State<String> = _userPhone


    fun getUserProfile(){
        _userName.value = sessionManager.getUserName() ?: ""
        _userAddress.value = sessionManager.getUserAddress() ?: ""
        _userPhone.value = sessionManager.getUserPhone() ?: ""
    }

    fun getCurrentUserId(): Long?{
        return sessionManager.getUserId()
    }

    fun logout() {
        sessionManager.clearSession()
    }

}