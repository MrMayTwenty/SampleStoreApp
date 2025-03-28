package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.sharedpreference.EncryptedSharedPreferenceManager
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.data.remote.dto.request.Amount
import com.aaa.samplestore.data.remote.dto.request.PayPalOrderRequest
import com.aaa.samplestore.data.remote.dto.request.PurchaseUnit
import com.aaa.samplestore.data.remote.dto.response.toPayPalOrderStatus
import com.aaa.samplestore.domain.model.PayPalOrderStatus
import com.aaa.samplestore.domain.repository.ICheckOutRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetPayPalOrderIdUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val checkOutRepository: ICheckOutRepository,
    private val cartDao: CartDao,
    private val sessionManager: SessionManager,
    private val encryptedSharedPreferenceManager: EncryptedSharedPreferenceManager
) {
    operator fun invoke(): Flow<Resource<PayPalOrderStatus>> = flow {
        try {
            emit(Resource.Loading())
            val payPalAuthResponse = checkOutRepository.getAccessToken()
            val cartItems = cartDao.getCartItems(sessionManager.getUserId()?.toInt() ?: 0)
            val purchaseUnits = cartItems.map { item ->
                PurchaseUnit(
                    referenceId = item.id.toString(),
                    amount = Amount(
                        currencyCode = "USD",
                        value = "${item.price * item.quantity}"
                    )
                )
            }
            val payPalOrderRequest = PayPalOrderRequest(
                purchaseUnits = purchaseUnits,
                intent = "CAPTURE"
            )
            val payPalOrderStatus =
                checkOutRepository.createOrderId("Bearer ${payPalAuthResponse.accessToken}", payPalOrderRequest).toPayPalOrderStatus()
            encryptedSharedPreferenceManager.saveOauthAccessToken(payPalAuthResponse.accessToken)
            emit(Resource.Success(payPalOrderStatus))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_general_fallback_text)))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_no_internet)))
        }
    }
}
