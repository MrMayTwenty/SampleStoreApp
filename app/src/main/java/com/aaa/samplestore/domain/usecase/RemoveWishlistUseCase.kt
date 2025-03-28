package com.aaa.samplestore.domain.usecase

import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.WishlistDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveWishlistUseCase @Inject constructor(
    private val wishlistDao: WishlistDao
) {
    operator fun invoke(id: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            wishlistDao.removeFromWishlist(id)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
}