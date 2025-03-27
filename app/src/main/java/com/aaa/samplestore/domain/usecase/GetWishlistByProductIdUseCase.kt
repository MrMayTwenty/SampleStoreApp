package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.WishlistDao
import com.aaa.samplestore.data.local.entity.toWishlistItem
import com.aaa.samplestore.domain.model.WishlistItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWishlistByProductIdUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val wishlistDao: WishlistDao
) {
    operator fun invoke(id: Int): Flow<Resource<WishlistItem>> = flow {
        try {
            emit(Resource.Loading())
            val wishlists = wishlistDao.getWishlistByProductId(id).toWishlistItem()
            emit(Resource.Success(wishlists))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_general_fallback_text)))
        }
    }
}
