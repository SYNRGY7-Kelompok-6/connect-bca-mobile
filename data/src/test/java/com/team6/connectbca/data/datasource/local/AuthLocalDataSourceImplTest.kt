package com.team6.connectbca.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthLocalDataSourceImplTest {
    private val dataStore = mock<DataStore<Preferences>>()
    private val authLocalDataSource = AuthLocalDataSourceImpl(dataStore = dataStore)
    private val companions = mock<AuthLocalDataSourceImpl.Companion>()

    @Test
    fun saveSessionData() = runTest {
        // given
        val username: String = "user001"
        val token: String = "token"

        // when
        val expected = Unit
        val actual = authLocalDataSource.saveSessionData(username, token)

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun clearToken() = runTest {
        // when
        val expected = Unit
        val actual = authLocalDataSource.clearToken()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun clearSessionTime() = runTest {
        // when
        val expected = Unit
        val actual = authLocalDataSource.clearSessionTime()

        // then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getToken() = runTest {
        // given
        val token = "token"
        val key = stringPreferencesKey(companions.KEY_TOKEN)

        // when
        whenever(dataStore.data).thenReturn(
            flowOf(
                preferencesOf(
                    key to token
                )
            )
        )

        val expected = token
        val actual = authLocalDataSource.getToken()

        // then
        Assert.assertEquals(expected, actual)
    }
}