package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.user.UserRemoteDataSource
import com.team6.connectbca.data.datasource.services.UserService
import com.team6.connectbca.data.model.body.UpdateProfileBody
import com.team6.connectbca.data.model.response.UserProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRemoteDataSourceImpl(
    private val userService: UserService
) : UserRemoteDataSource {
    override suspend fun getUserProfile(): UserProfileResponse {
        return userService.getUserProfile()
    }

    override suspend fun updateUserProfile(updateProfileBody: UpdateProfileBody): UserProfileResponse {
        val file = updateProfileBody.image

        val imageReq = if (file != null) {
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
        } else {
            null
        }

        return userService.updateUserProfile(
            updateProfileBody.name.orEmpty().toRequestBody("text/plain".toMediaTypeOrNull()),
            updateProfileBody.email.orEmpty().toRequestBody("text/plain".toMediaTypeOrNull()),
            updateProfileBody.phone.orEmpty().toRequestBody("text/plain".toMediaTypeOrNull()),
            updateProfileBody.birth.orEmpty().toRequestBody("text/plain".toMediaTypeOrNull()),
            updateProfileBody.address.orEmpty().toRequestBody("text/plain".toMediaTypeOrNull()),
            imageReq
        )
    }

}