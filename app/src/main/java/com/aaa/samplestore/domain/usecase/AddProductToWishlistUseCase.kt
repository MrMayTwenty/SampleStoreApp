package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.WishlistDao
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.domain.model.WishlistItem
import com.aaa.samplestore.domain.model.toEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddProductToWishlistUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val wishlistDao: WishlistDao
) {
    operator fun invoke(wishlistItem: WishlistItem, userId: Long?): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            wishlistDao.addToWishlist(wishlistItem.toEntity())
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_general_fallback_text)))
        }
    }
}
