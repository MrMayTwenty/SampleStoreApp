package com.aaa.samplestore.presentation.wishlist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.WishlistDao
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.domain.model.WishlistItem
import com.aaa.samplestore.domain.usecase.GetWishlistByUserIdUseCase
import com.aaa.samplestore.presentation.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistByUserIdUseCase: GetWishlistByUserIdUseCase,
    private val sessionManager: SessionManager,
): ViewModel() {

    private val _wishlistState = mutableStateOf(ViewModelState<List<WishlistItem>>())
    val wishlistState: State<ViewModelState<List<WishlistItem>>> = _wishlistState

    fun getWishlist(){
        val currentUserId = sessionManager.getUserId()
        viewModelScope.launch {
            getWishlistByUserIdUseCase.invoke(currentUserId).collect { result ->
                when(result){
                    is Resource.Error -> _wishlistState.value = ViewModelState(error = result.message)
                    is Resource.Loading ->  _wishlistState.value = ViewModelState(isLoading = true)
                    is Resource.Success ->  _wishlistState.value = ViewModelState(data = result.data)
                }
            }
        }
    }

    fun removeProductFromWishlist(wishListId: Int) {}
}
