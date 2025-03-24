package com.aaa.samplestore.domain.usecase

import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.domain.repository.ICartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    operator fun invoke(cartId: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            cartRepository.removeFromCart(cartId)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
}