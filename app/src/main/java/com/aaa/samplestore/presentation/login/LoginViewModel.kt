package com.aaa.samplestore.presentation.login

import androidx.lifecycle.ViewModel
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.domain.usecase.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val LoginUserUseCase: LoginUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    fun isLoggedIn(): Boolean {
        return sessionManager.getUserId()?.let { it > 0L } == true
    }

    fun login(){

    }

    fun login(email: String, password: String) {

    }
}
