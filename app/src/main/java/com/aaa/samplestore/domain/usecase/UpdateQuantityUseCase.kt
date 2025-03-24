package com.aaa.samplestore.domain.usecase

import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.domain.repository.ICartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateQuantityUseCase @Inject constructor(
    private val cartRepository: ICartRepository
){
    operator fun invoke(cartId: Int, quantity: Int):Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            cartRepository.updateQuantity(cartId, quantity)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
}
