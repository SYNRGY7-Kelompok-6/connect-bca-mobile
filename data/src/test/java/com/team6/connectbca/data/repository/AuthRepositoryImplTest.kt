package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.auth.AuthLocalDataSource
import com.team6.connectbca.data.datasource.interfaces.auth.AuthRemoteDataSource
import com.team6.connectbca.data.model.body.LoginBody
import com.team6.connectbca.data.model.body.PinBody
import com.team6.connectbca.data.model.response.LoginResponse
import com.team6.connectbca.data.model.response.LoginResponseData
import com.team6.connectbca.data.model.response.PinDataResponse
import com.team6.connectbca.data.model.response.PinResponse
import com.team6.connectbca.domain.model.Pin
import com.team6.connectbca.domain.model.PinData
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthRepositoryImplTest {
    private val authRemoteDataSource = mock<AuthRemoteDataSource>()
    private val authLocalDataSource = mock<AuthLocalDataSource>()

    private val authRepository = AuthRepositoryImpl(
        authLocalDataSource = authLocalDataSource,
        authRemoteDataSource = authRemoteDataSource
    )

    @Test
    fun userLogin() = runTest {
        // given
        val username: String = "user001"
        val password: String = "Password_0"
        val loginBody = LoginBody(username, password)
        val loginResponseData = LoginResponseData(accessToken = "anuanu")
        val loginResponse = LoginResponse(
            status = "", message = "", data = loginResponseData
        )

        // when
        whenever(authRemoteDataSource.userLogin(loginBody)).thenReturn(loginResponse)

        val expected = true
        val actual = authRepository.userLogin(username, password)

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun saveSessionData() = runTest {
        // given
        val username: String = "user001"
        val token: String = "token"

        // when
        whenever(authLocalDataSource.saveSessionData(username, token)).thenReturn(Unit)

        val expected = Unit
        val actual = authRepository.saveSessionData(username, token)

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getSessionData() = runTest {
        // given
        val sessionData = mapOf<String, Any>()

        // when
        whenever(authLocalDataSource.getSessionData()).thenReturn(sessionData)

        val expected = sessionData
        val actual = authRepository.getSessionData()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun clearToken() = runTest {
        // when
        whenever(authLocalDataSource.clearToken()).thenReturn(Unit)

        val expected = Unit
        val actual = authRepository.clearToken()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun clearSessionTime() = runTest {
        // when
        whenever(authLocalDataSource.clearSessionTime()).thenReturn(Unit)

        val expected = Unit
        val actual = authRepository.clearSessionTime()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun pinValidation() = runTest {
        // given
        val pin = "123456"
        val pinBody = PinBody(pin = pin)
        val pinData = PinData(pinToken = "arigatou")
        val pinDataResponse = PinDataResponse(pinToken = "arigatou")
        val pinResponse = PinResponse(status = "", message = "", data = pinDataResponse)
        val pinEntity = Pin(status = "", message = "", data = pinData)

        // when
        whenever(authRemoteDataSource.pinToken(pinBody)).thenReturn(pinResponse)

        val expected = pinEntity
        val actual = authRepository.pinValidation(pin)

        // then
        Assert.assertEquals(expected, actual)
    }
}