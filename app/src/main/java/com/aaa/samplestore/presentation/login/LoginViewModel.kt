package com.aaa.samplestore.presentation.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    fun isLoggedIn(): Boolean {
        return false
    }

    fun login(){

    }
}
