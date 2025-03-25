package com.aaa.samplestore.domain.usecase

import android.content.Context
import com.aaa.samplestore.R
import com.aaa.samplestore.common.Resource
import com.aaa.samplestore.data.remote.dto.request.AddUserRequest
import com.aaa.samplestore.data.remote.dto.response.toProductList
import com.aaa.samplestore.data.remote.dto.response.toUser
import com.aaa.samplestore.domain.model.User
import com.aaa.samplestore.domain.repository.IUserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: IUserRepository
) {
    operator fun invoke(request: AddUserRequest): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading())
            val response = userRepository.createUser(request)
            if(response.status == "SUCCESS"){
                emit(Resource.Success(response.user.toUser()))
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