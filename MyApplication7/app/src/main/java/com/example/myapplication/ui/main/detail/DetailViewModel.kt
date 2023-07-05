package com.example.myapplication.ui.main.detail

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.ui.main.model.PhotoDetails
import com.example.myapplication.ui.main.repository.PhotoRemoteRepositoryImpl
import com.example.myapplication.ui.main.state.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class DetailViewModel : ViewModel() {

    private val _state = MutableStateFlow<DetailsState>(DetailsState.NotStartedYet)
    val state = _state.asStateFlow()

    var downloadID = 0L
    var downloading = true
    var success = false
val repositoryImpl = PhotoRemoteRepositoryImpl()
    val _loadState = MutableStateFlow(LoadState.START)
    val loadState = _loadState.asStateFlow()

   val handler = CoroutineExceptionHandler { _, _ ->
        _loadState.value = LoadState.ERROR
    }
    fun loadPhotoDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            _loadState.value = LoadState.SUCCESS
            _state.value = DetailsState.Success(repositoryImpl.getPhotoDetail (id = id))
        }
    }


    fun startDownLoad(url: String, downloadManager: DownloadManager) {
        val downloadRequest = DownloadManager.Request(Uri.parse(url))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setTitle("Unsplash photo")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                File.separator + "fromUnsplash.jpg"
            )
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        downloadID = downloadManager.enqueue(downloadRequest)
    }

    @SuppressLint("Range")
    fun getDMStatus(myDownloadManager: DownloadManager) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            val request = DownloadManager.Query().setFilterById(downloadID)
            var cursor: Cursor?
            request.setFilterByStatus(DownloadManager.STATUS_FAILED or DownloadManager.STATUS_SUCCESSFUL)
            while (downloading) {
                cursor = myDownloadManager.query(request)
                if (cursor.moveToPosition(0) && downloading) {
                    Log.i("Kart", "Downloading")
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false
                        success = true
                        cursor.close()
                    }
                    if (status == DownloadManager.STATUS_FAILED) {
                        downloading = false
                        success = false
                        cursor.close()
                    }
                } else {
                    cursor.close()
                }
            }
            Log.i("Kart", "Success = $success")
        }
    }
}