package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.dao.UserDao
import com.aaa.samplestore.data.local.dao.WishlistDao
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.data.remote.dto.request.AddUserRequest
import com.aaa.samplestore.data.remote.dto.request.toEntity
import com.aaa.samplestore.data.remote.dto.request.toUser
import com.aaa.samplestore.data.remote.dto.response.toProductList
import com.aaa.samplestore.domain.repository.IUserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mindrot.jbcrypt.BCrypt
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: IUserRepository,
    private val userDao: UserDao,
    private val cartDao: CartDao,
    private val wishlistDao: WishlistDao,
    private val sessionManager: SessionManager
) {
    operator fun invoke(request: AddUserRequest): Flow<Resource<Int>> = flow {
        try {
            emit(Resource.Loading())
            val updatedRequest = request.copy(password = BCrypt.hashpw(request.password, BCrypt.gensalt(12)))
            val response = userRepository.createUser(updatedRequest)
            if(response.status == "SUCCESS"){
                val newUserId = userDao.insertUser(request.toEntity())
                cartDao.assignUserToUnownedCarts(newUserId.toInt())
                wishlistDao.assignUserToUnownedWishlists(newUserId.toInt())
                sessionManager.saveUserId(newUserId)
                sessionManager.saveUserDetails(request.toUser())
                emit(Resource.Success(response.user.id))
                return@flow
            }
            emit(Resource.Error(response.message))
        } catch (e: HttpException){
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_general_fallback_text)))
        } catch (e: IOException){
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_no_internet)))
        }
    }
}