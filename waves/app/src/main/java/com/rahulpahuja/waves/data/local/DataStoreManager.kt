package com.rahulpahuja.waves.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager @Inject constructor(private val context: Context) {

    companion object {
        val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        val THEME_KEY      = stringPreferencesKey("selected_theme")
        val ROLE_KEY       = stringPreferencesKey("user_role")
    }

    suspend fun saveUserToken(token: String) {
        context.dataStore.edit { it[USER_TOKEN_KEY] = token }
    }

    suspend fun saveTheme(themeName: String) {
        context.dataStore.edit { it[THEME_KEY] = themeName }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { it[ROLE_KEY] = role }
    }

    val userToken: Flow<String?> = context.dataStore.data.map { it[USER_TOKEN_KEY] }
    val selectedTheme: Flow<String?> = context.dataStore.data.map { it[THEME_KEY] }
    val userRole: Flow<String?> = context.dataStore.data.map { it[ROLE_KEY] }
}
