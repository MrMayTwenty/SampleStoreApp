package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.sharedpreference.EncryptedSharedPreferenceManager
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.data.remote.PayPalApi
import com.aaa.samplestore.data.remote.dto.PayPalCaptureResponse
import com.aaa.samplestore.data.remote.dto.request.Amount
import com.aaa.samplestore.data.remote.dto.request.PayPalOrderRequest
import com.aaa.samplestore.data.remote.dto.request.PurchaseUnit
import com.aaa.samplestore.data.remote.dto.response.toPayPalOrderStatus
import com.aaa.samplestore.data.repository.CheckOutRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CapturePaymentUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val checkOutRepository: CheckOutRepository,
    private val encryptedSharedPreferenceManager: EncryptedSharedPreferenceManager,
    private val sessionManager: SessionManager,
    private val cartDao: CartDao
) {
    suspend operator fun invoke(orderId: String): Flow<Resource<PayPalCaptureResponse>> = flow {
        try {
            emit(Resource.Loading())
            val payPalOAuthToken = encryptedSharedPreferenceManager.getOAuthAccessToken()
            val response = checkOutRepository.captureOrder("Bearer $payPalOAuthToken", orderId = orderId)
            cartDao.clearUserCart(sessionManager.getUserId()?.toInt() ?: 0)
            emit(Resource.Success(response))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_general_fallback_text)))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_no_internet)))
        }
    }
}
