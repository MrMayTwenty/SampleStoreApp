package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.remote.dto.response.toProductList
import com.aaa.samplestore.domain.model.Product
import com.aaa.samplestore.domain.repository.IProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetProductByCategoryUseCase @Inject constructor(
    private val productRepository: IProductRepository,
    @ApplicationContext private val context: Context
) {
 operator fun invoke(category: String): Flow<Resource<List<Product>>> = flow {
     try {
         emit(Resource.Loading())
         val productsResponse = productRepository.getProductsByCategory(category)
         if (productsResponse.status == "SUCCESS") {
             emit(Resource.Success(productsResponse.toProductList()))
             return@flow
         }
         emit(Resource.Error(productsResponse.message))
     } catch (e: HttpException) {
         emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_general_fallback_text)))
     } catch (e: IOException) {
         emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_no_internet)))
     }
 }
}
