package com.aaa.samplestore.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.dao.CartDao_Impl
import com.aaa.samplestore.data.local.dao.UserDao
import com.aaa.samplestore.data.local.dao.WishlistDao
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.data.remote.dto.request.AddUserRequest
import com.aaa.samplestore.data.remote.dto.request.toEntity
import com.aaa.samplestore.data.remote.dto.request.toUser
import com.aaa.samplestore.domain.model.User
import com.aaa.samplestore.domain.model.toEntity
import com.aaa.samplestore.domain.usecase.AddUserUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val addUserUseCase: AddUserUseCase,
) : ViewModel() {

    private val _userState = mutableStateOf(ViewModelState<User>())
    val userState: State<ViewModelState<User>> = _userState

    fun registerUser(request: AddUserRequest) {
        viewModelScope.launch {
            addUserUseCase.invoke(request).collect { result ->
                when (result) {
                    is Resource.Error -> _userState.value = ViewModelState(error = result.message)
                    is Resource.Loading -> _userState.value = ViewModelState(isLoading = true)
                    is Resource.Success -> _userState.value = ViewModelState(data = request.toUser())
                }
            }
        }
    }

}
