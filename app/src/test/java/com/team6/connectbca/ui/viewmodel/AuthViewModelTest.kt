package com.team6.connectbca.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.mycatcollections.MainDispatcherRule
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.domain.usecase.LoginUseCase
import com.team6.connectbca.domain.usecase.LogoutUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase = mock<LoginUseCase>()
    private val logoutUseCase = mock<LogoutUseCase>()
    private val getSessionUseCase = mock<GetSessionDataUseCase>()
    private val authViewModel = AuthViewModel(
        loginUseCase = loginUseCase,
        logoutUseCase = logoutUseCase,
        getSessionDataUseCase = getSessionUseCase
    )

    // observers
    private val loadingObserver = mock<Observer<Boolean>>()
    private val errorObserver = mock<Observer<Throwable>>()
    private val successObserver = mock<Observer<Boolean>>()
    private val sessionDataObserver = mock<Observer<Map<String, Any>>>()

    // captors
    private val loadingCaptor = argumentCaptor<Boolean>()
    private val errorCaptor = argumentCaptor<Throwable>()
    private val successCaptor = argumentCaptor<Boolean>()
    private val sessionDataCaptor = argumentCaptor<Map<String, Any>>()

    @Test
    fun userLoginSuccess() = runTest {
        // given
        val username = "user001"
        val password = "Password_0"
        val loadingLiveData = authViewModel.getLoading()
        val successLiveData = authViewModel.getSuccess()

        loadingLiveData.observeForever(loadingObserver)
        successLiveData.observeForever(successObserver)

        // when
        whenever(loginUseCase(username, password)).thenReturn(true)
        authViewModel.userLogin(username, password)

        // then
        verify(loadingObserver, times(2)).onChanged(loadingCaptor.capture())
        verify(successObserver).onChanged(successCaptor.capture())

        Assert.assertEquals(loadingCaptor.allValues, listOf(true, false))
        Assert.assertEquals(successCaptor.allValues, listOf(true))
    }

    @Test
    fun userLoginFailed() = runTest {
        // given
        val username = "user001"
        val password = "Password_0"
        val loadingLiveData = authViewModel.getLoading()
        val errorLiveData = authViewModel.getError()

        loadingLiveData.observeForever(loadingObserver)
        errorLiveData.observeForever(errorObserver)

        // when
        val errorThrowable = UnsupportedOperationException("Terdapat kesalahan saat login")

        whenever(loginUseCase(username, password)).thenThrow(errorThrowable)
        authViewModel.userLogin(username, password)

        // then
        verify(errorObserver).onChanged(errorCaptor.capture())
        verify(loadingObserver, times(2)).onChanged(loadingCaptor.capture())

        Assert.assertEquals(loadingCaptor.allValues, listOf(true, false))
        Assert.assertEquals(errorCaptor.allValues, listOf(errorThrowable))
    }

    @Test
    fun getUserSessionDataSuccess() = runTest {
        // given
        val sessionLiveData = authViewModel.getUserSessionData()

        sessionLiveData.observeForever(sessionDataObserver)

        // when
        whenever(getSessionUseCase()).thenReturn(mapOf<String, Any>())
        authViewModel.getUserSessionData()

        // then
        verify(sessionDataObserver).onChanged(sessionDataCaptor.capture())

        Assert.assertEquals(sessionDataCaptor.allValues, listOf(mapOf<String, Any>()))
    }

    @Test
    fun getUserSessionDataFailed() = runTest {
        // given
        val errorLiveData = authViewModel.getError()

        errorLiveData.observeForever(errorObserver)

        // when
        val errorThrowable = UnsupportedOperationException("Terdapat kesalahan saat mengambil data")

        // when
        whenever(getSessionUseCase()).thenThrow(errorThrowable)
        authViewModel.getUserSessionData()

        // then
        verify(errorObserver).onChanged(errorCaptor.capture())

        Assert.assertEquals(errorCaptor.allValues, listOf(errorThrowable))
    }
}