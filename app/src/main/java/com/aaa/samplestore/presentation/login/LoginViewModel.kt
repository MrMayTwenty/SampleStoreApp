package com.aaa.samplestore.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.domain.model.User
import com.aaa.samplestore.domain.usecase.LoginUserUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUserUseCase: LoginUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _userState = mutableStateOf(ViewModelState<User>())
    val userState: State<ViewModelState<User>> = _userState

    fun isLoggedIn(): Boolean {
        return sessionManager.getUserId()?.let { it > 0L } == true
    }

    fun login(email: String, password: String){
        viewModelScope.launch {
            loginUserUseCase.invoke(email, password).collect { result ->
                when(result){
                    is Resource.Error<*> -> _userState.value = ViewModelState(error = result.message)
                    is Resource.Loading<*> -> _userState.value = ViewModelState(isLoading = true)
                    is Resource.Success<*> -> _userState.value = ViewModelState(data = result.data)
                }
            }
        }
    }

}
