package com.example.myapplication.ui.main.auth

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.myapplication.ui.main.photolistnew


class AuthRepo(app: Application) {
    
    companion object {
        const val PREFERENCES_STORE_NAME = "tokens_store"
        const val AUTH_TOKEN_KEY = "auth_token_key"
    }
    
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { app.preferencesDataStoreFile(PREFERENCES_STORE_NAME) }
    )
    
    suspend fun saveAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(AUTH_TOKEN_KEY)] = token
        }
        var log = mapOf<Preferences.Key<*>, Any>()
        dataStore.edit {
            log = it.asMap()
        }
    }
    
    suspend fun checkAuthToken(): AuthCheckResult<Boolean> {
        return AuthCheckResult.Result(dataStore.edit {}.contains(stringPreferencesKey(AUTH_TOKEN_KEY)))
    }
    
    suspend fun getAccessToken(): String? {
        return dataStore.edit { }[stringPreferencesKey(AUTH_TOKEN_KEY)]
    }
    
    suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }
    
}



/*{
        suspend fun getToken(code: String): AuthInfo {
            Log.d(ContentValues.TAG, "getPhotoList:")
            return  retrofitT.getToken(code)
        }
}*/