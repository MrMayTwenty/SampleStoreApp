package com.aaa.samplestore.domain.usecase

import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.domain.model.CartItem
import com.aaa.samplestore.domain.repository.ICartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    operator fun invoke(item: CartItem, userId: Int?): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val existingItem = if(userId != null) {
                cartRepository.getCartItems(userId).firstOrNull { it.cartId != null && it.productId == item.productId }
            } else {
                cartRepository.getCartItems().firstOrNull { it.cartId != null && it.productId == item.productId }
            }

            if (existingItem != null) {
                cartRepository.updateQuantity(existingItem.cartId, existingItem.quantity + item.quantity)
                emit(Resource.Success(Unit))
                return@flow
            }

            cartRepository.addToCart(item)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }
}
