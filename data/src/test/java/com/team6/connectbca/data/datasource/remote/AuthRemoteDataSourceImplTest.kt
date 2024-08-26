package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.services.LoginService
import com.team6.connectbca.data.datasource.services.PinService
import com.team6.connectbca.data.model.body.LoginBody
import com.team6.connectbca.data.model.body.PinBody
import com.team6.connectbca.data.model.response.LoginResponse
import com.team6.connectbca.data.model.response.LoginResponseData
import com.team6.connectbca.data.model.response.PinDataResponse
import com.team6.connectbca.data.model.response.PinResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthRemoteDataSourceImplTest {
    private val loginService = mock<LoginService>()
    private val pinService = mock<PinService>()
    private val authRemoteDataSource = AuthRemoteDataSourceImpl(
        loginService = loginService, pinService = pinService
    )

    @Test
    fun userLogin() = runTest {
        // given
        val loginBody = LoginBody(userID = "user001", password = "Password_0")
        val loginResponseData = LoginResponseData(accessToken = "token")
        val loginResponse = LoginResponse(status = "", message = "", data = loginResponseData)

        // when
        whenever(loginService.userLogin(loginBody)).thenReturn(loginResponse)

        val expected = loginResponse
        val actual = authRemoteDataSource.userLogin(loginBody)

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun pinToken() = runTest {
        // given
        val pinBody = PinBody("123456")
        val pinResponseData = PinDataResponse(pinToken = "123456")
        val pinResponse = PinResponse(status = "", message = "", data = pinResponseData)

        // when
        whenever(pinService.pinValidate(pinBody)).thenReturn(pinResponse)

        val expected = pinResponse
        val actual = authRemoteDataSource.pinToken(pinBody)

        // then
        Assert.assertEquals(expected, actual)
    }
}