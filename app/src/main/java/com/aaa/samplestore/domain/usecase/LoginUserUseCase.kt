package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.dao.UserDao
import com.aaa.samplestore.data.local.dao.WishlistDao
import com.aaa.samplestore.data.local.entity.toUser
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDao: UserDao,
    private val cartDao: CartDao,
    private val wishlistDao: WishlistDao,
    private val sessionManager: SessionManager
) {
    operator fun invoke(email: String, password: String): Flow<Resource<User>> = flow {
        try {
            val user = userDao.getUser(email)?.toUser()
            if(user != null) {
                val isPasswordVerified = BCrypt.checkpw(password, user.password)
                if(!isPasswordVerified){
                    emit(Resource.Error(context.getString(R.string.account_not_found)))
                    return@flow
                }
                cartDao.assignUserToUnownedCarts(user.id)
                wishlistDao.assignUserToUnownedWishlists(user.id)
                sessionManager.saveUserId(user.id.toLong())
                sessionManager.saveUserDetails(user)
                emit(Resource.Success(user))
                return@flow
            }
            emit(Resource.Error(context.getString(R.string.error_general_fallback_text)))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_general_fallback_text)))
        }
    }
}
