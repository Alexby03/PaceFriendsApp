package com.kth.stepapp.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val EMAIL_KEY = stringPreferencesKey("user_email")
        val PASSWORD_KEY = stringPreferencesKey("user_password")
    }

    val userCredentials: Flow<Pair<String?, String?>> = dataStore.data
        .map { preferences ->
            val email = preferences[EMAIL_KEY]
            val password = preferences[PASSWORD_KEY]
            Pair(email, password)
        }

    suspend fun saveUser(email: String, pass: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
            preferences[PASSWORD_KEY] = pass
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
