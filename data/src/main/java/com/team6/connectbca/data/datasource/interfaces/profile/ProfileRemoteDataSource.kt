package com.team6.connectbca.data.datasource.interfaces.profile

interface ProfileRemoteDataSource {
    suspend fun getUserProfileInfo()
}