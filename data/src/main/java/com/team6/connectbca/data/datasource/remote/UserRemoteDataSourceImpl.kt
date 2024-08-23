package com.team6.connectbca.data.datasource.remote

import android.util.Log
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
        var imageReq: MultipartBody.Part? = null
        Log.i("REMOTE: ISI FILE", file?.absolutePath ?: "null dia")

        if (file != null) {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

            Log.i("REMOTE: REQUEST FILE", requestFile.toString())

            imageReq = MultipartBody.Part.createFormData(
                "image",
                file.name,
                requestFile
            )
        }

        Log.i("REMOTE: CONTENT TYPE", imageReq?.body?.contentType().toString())
        Log.i("REMOTE: CONTENT SIZE", imageReq?.body?.contentLength().toString())

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