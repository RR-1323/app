package com.example.myapplication.ui.main.auth.interceptor


import android.content.ContentValues.TAG
import android.util.Log
import com.example.myapplication.ui.main.auth.TokenStorage
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
                Log.d(TAG, "token = " + token.toString())
//val token = "Sg9xeSqIq65judRStv7Hu-hCRt2Fwa_fTtmp4UuC45k"
                if (token != null) {
                    header(authHeaderName, token.withBearer())
                }
            }
            .build()
    }

    private fun String.withBearer() = "Bearer $this"


}

