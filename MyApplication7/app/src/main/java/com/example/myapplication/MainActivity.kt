package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.main.MainFragment


import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
    /*

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let{
            handleDeepLink(it)
            Log.d("onNewIntent", "$authcode")
        }

        val newIntent = Intent(this@MainActivity, MainActivity::class.java)
        startActivity(newIntent)
    }

    companion object {
        // dummy request code to identify the request
        private const val REQUEST_CODE = 123
    }

    fun handleDeepLink(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW) return
        val deepLinkUri = intent.data ?: return
        if (deepLinkUri.queryParameterNames.contains("code")) {
            authcode = deepLinkUri.getQueryParameter("code") ?: return
         getToken(authcode)
            //  viewModel.createToken(authcode)
        }
    }

    fun getToken(authCode: String) {
       lifecycleScope.launch(Dispatchers.IO) {
        //    _loadState.emit(LoadState.START)
            Log.d("onIntent", "getToken ${authCode}")
            kotlin.runCatching {
                retrofitTok.getToken(code = authCode)

            }.fold(
                onSuccess = {
                    accessToken = it.access_token
                    Log.d("onIntent", "success token" + it.access_token)
                  //  _loadState.emit(LoadState.SUCCESS)
                },
                onFailure = {
                    Log.d("onIntent", "error token" + it.message ?: "")
                   // _loadState.emit(LoadState.ERROR)
                }
            )
        }
    }*/
}