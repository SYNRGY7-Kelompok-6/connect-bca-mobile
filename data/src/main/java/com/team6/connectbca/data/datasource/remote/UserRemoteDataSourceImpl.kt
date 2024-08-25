package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.user.UserRemoteDataSource
import com.team6.connectbca.data.datasource.services.UserService
import com.team6.connectbca.data.model.body.UpdateProfileBody
import com.team6.connectbca.data.model.response.UserProfileResponse
import okhttp3.MultipartBody

class UserRemoteDataSourceImpl(
    private val userService: UserService
) : UserRemoteDataSource {
    override suspend fun getUserProfile(): UserProfileResponse {
        return userService.getUserProfile()
    }

    override suspend fun updateUserProfile(updateProfileBody: UpdateProfileBody): UserProfileResponse {
        val multipartBody = addMultipartBuilder(updateProfileBody)

        return userService.updateUserProfile(multipartBody)
    }

    private fun addMultipartBuilder(
        updateProfileBody: UpdateProfileBody
    ) : MultipartBody {
        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", updateProfileBody.name.orEmpty())
            .addFormDataPart("email", updateProfileBody.email.orEmpty())
            .addFormDataPart("phone", updateProfileBody.phone.orEmpty())
            .addFormDataPart("birth", updateProfileBody.birth.orEmpty())
            .addFormDataPart("address", updateProfileBody.address.orEmpty())
            .build()
    }

}