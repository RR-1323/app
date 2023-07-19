package com.example.myapplication.ui.main.auth.interceptor


import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile

import com.example.myapplication.ui.main.auth.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthTokenInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .addTokenHeader()
            .let { chain.proceed(it) }
    }

    private fun Request.addTokenHeader(): Request {
        val authHeaderName = "Authorization"
        return newBuilder()
            .apply {

                val token = TokenStorage.accessToken
//val token = "Sg9xeSqIq65judRStv7Hu-hCRt2Fwa_fTtmp4UuC45k"
                if (token != null) {
                    header(authHeaderName, token.withBearer())
                }
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"


}

