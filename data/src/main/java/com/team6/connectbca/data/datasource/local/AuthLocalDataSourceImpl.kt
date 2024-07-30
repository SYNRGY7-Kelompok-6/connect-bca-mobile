package com.team6.connectbca.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.team6.connectbca.data.datasource.interfaces.auth.AuthLocalDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AuthLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : AuthLocalDataSource {
    companion object {
        const val KEY_TOKEN = "token"
        const val KEY_USERID = "userId"
        const val KEY_SESSION_TIME = "sessionTime"
        private val DATASTORE_KEY_TOKEN = stringPreferencesKey(KEY_TOKEN)
        private val DATASTORE_KEY_USERID = stringPreferencesKey(KEY_USERID)
        private val DATASTORE_KEY_SESSION_TIME = longPreferencesKey(KEY_SESSION_TIME)
    }

    override suspend fun saveSessionData(userId: String, token: String) {
        setToken(token)
        setUserId(userId)
        setUserSessionTime()
    }

    override suspend fun getSessionData() : Map<String, Any> {
        val userId = getUserId() ?: ""
        val token = getToken() ?: ""
        val sessionTime = getUserSessionTime() ?: 0

        return mapOf(
            KEY_USERID to userId,
            KEY_TOKEN to token,
            KEY_SESSION_TIME to sessionTime
        )
    }

    override suspend fun checkUserId(userId: String): Boolean {
        val storedUserId: String? = getUserId()

        if ((!storedUserId.isNullOrEmpty())) {
             if (storedUserId.compareTo(userId) == 0) {
                 return true
             }
        }

        return false
    }

    override suspend fun clearToken() {
        dataStore.edit { pref ->
            pref[DATASTORE_KEY_TOKEN] = ""
        }
    }

    private suspend fun setToken(token: String) {
        dataStore.edit { pref ->
            pref[DATASTORE_KEY_TOKEN] = token
        }
    }

    private suspend fun setUserId(userId: String) {
        dataStore.edit { pref ->
            pref[DATASTORE_KEY_USERID] = userId
        }
    }

    private suspend fun setUserSessionTime() {
        dataStore.edit { pref ->
            pref[DATASTORE_KEY_SESSION_TIME] = System.currentTimeMillis()
        }
    }

    private suspend fun getToken() : String? {
        return dataStore.data.map { pref ->
            pref[DATASTORE_KEY_TOKEN]
        }.firstOrNull()
    }

    private suspend fun getUserId() : String? {
        return dataStore.data.map { pref ->
            pref[DATASTORE_KEY_USERID]
        }.firstOrNull()
    }

    private suspend fun getUserSessionTime() : Long? {
        return dataStore.data.map { pref ->
            pref[DATASTORE_KEY_SESSION_TIME]
        }.firstOrNull()
    }
}