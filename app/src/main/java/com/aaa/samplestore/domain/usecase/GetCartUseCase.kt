package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.entity.toCartItem
import com.aaa.samplestore.domain.model.CartItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cartDao: CartDao
) {
    operator fun invoke():Flow<Resource<List<CartItem>>> = flow {
        try {
            emit(Resource.Loading())
            val cartItems = cartDao.getCartItems().map { it.toCartItem() }
            emit(Resource.Success(cartItems))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_general_fallback_text)))
        }
    }
}