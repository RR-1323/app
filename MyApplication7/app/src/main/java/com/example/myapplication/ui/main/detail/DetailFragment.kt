package com.example.myapplication.ui.main.detail

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailBinding
import com.example.myapplication.ui.main.model.PhotoDetails
import com.example.myapplication.ui.main.state.LoadState
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private var lat: Double? = null
    private var lon: Double? = null
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var receiver: BroadcastReceiver
    private val viewModel: DetailViewModel by viewModels()
    private val downloadManager by lazy {
        requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
    private var enableDownloadFlag = false
    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.isNotEmpty() && map.values.all { it }) {
            Snackbar.make(
                binding.myCoordinatorLayout,
                "getString(R.string.ok_to_download)",
                Snackbar.LENGTH_LONG
            ).show()
            enableDownloadFlag = true
        } else {
            showMissingPermissionAlert()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val id = arguments?.getString("id")
        viewLifecycleOwner.lifecycleScope.launch {
          val photo =  viewModel.loadPhoto(id.toString())
            binding.isLiked.setOnClickListener{
                if (photo.likedByUser) {
                    viewModel.unliked(photo.id)
                } else {
                    viewModel.liked(photo.id)
                }
                setLikeClick(photo)
            }
            }

        viewModel.loadPhotoDetails(id.toString())
        updateUi()
        getLoadingState()
        setLocationClick()
        loadStateLike()

    }

    private fun loadStateLike() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadState.collect { loadStateLike ->
                binding.error.isVisible =
                    loadStateLike == LoadState.ERROR
            }
        }
    }

    private fun setLikeClick(item: PhotoDetails) {

        viewModel.changeLike(item)
    }


    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                val reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (viewModel.downloadID == reference) {
                    viewModel.getDMStatus(downloadManager)
                    while (viewModel.downloading) {
                        //Log.d("Kart", ".")
                    }
                    if (viewModel.success) {
                        val uri = downloadManager.getUriForDownloadedFile(viewModel.downloadID)
                        showSuccessfulDownloadSnackbar(uri)
                    } else {
                        showFailedDownloadSnackbar()
                    }
                }
            }
        }



        registerReceiver(requireContext(), receiver, filter, RECEIVER_EXPORTED)
    }

    private fun showFailedDownloadSnackbar() {
        Snackbar.make(
            binding.myCoordinatorLayout,
            " getString(R.string.failed_download)",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setUploadedLocation(state: DetailsState.Success) {
        if (state.data.location.position.latitude != null && state.data.location.position.longitude != null) {
            lat = state.data.location.position.latitude
            lon = state.data.location.position.longitude
        }
    }

    private fun getLoadingState() {
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.loadState.collect { loadState -> updateUiOnServerResponse(loadState) }
            }
    }

    private fun updateUiOnServerResponse(loadState: LoadState) {
        if (loadState == LoadState.ERROR) {
            binding.error.isVisible = true
            binding.scroll.isVisible = false
        }
        if (loadState == LoadState.SUCCESS) updateUi()
    }

    private fun showMissingPermissionAlert() {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("getString(R.string.alert_title)")
        alertDialog.setMessage("getString(R.string.alert_text)")
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
        alertDialog.setButton(
            DialogInterface.BUTTON_POSITIVE,
            " getString(R.string.ok)"
        ) { dialog, _ ->
            dialog.cancel()
        }
        alertDialog.show()
    }

    private fun updateUi() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                when (state) {
                    DetailsState.NotStartedYet -> {}
                    is DetailsState.Success -> {
                        bindUploadedTexts(state)
                        bindUploadedImages(state)
                        setUploadedLocation(state)
                        setToolbar(state.data.id)
                        setLikeClick(state.data)
                        binding.isLiked.isSelected = state.data.likedByUser
                        setDownloadOnClick(state.data.urls.raw, downloadManager)
                    }

                }
            }
        }
    }

    private fun setToolbar(id: String) {
        val button = binding.shareBar.menu.getItem(0)
        button?.setOnMenuItemClickListener {
            shareLinkOnPhoto(id)
            true
        }
    }

    private fun shareLinkOnPhoto(id: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://unsplash.com/photos/$id")
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share)))
    }

    private fun bindUploadedImages(state: DetailsState.Success) {
        binding.photo.loadImage(state.data.urls.regular)
        binding.authorAvatar.loadImage(state.data.user.profileImage.small)
    }

    fun ImageView.loadImage(urls: String) {
        Glide.with(this)
            .load(urls)
            .error(R.drawable.error_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.placeholder)
            .into(this)
    }

    @SuppressLint("StringFormatInvalid")
    private fun bindUploadedTexts(state: DetailsState.Success) {


        binding.authorName.text = state.data.user.name
        binding.authorAccount.text = getString(R.string.author_account, state.data.user.username)

        binding.location.text = state.data.location.city ?: "N/A"
        binding.currentLikes.text = state.data.likes.toString()
        binding.isLiked.isSelected = state.data.likedByUser

        binding.tags.text = state.data.tags.joinToString { tag ->
            "#${tag.title ?: "N/A"}"
        }
        binding.exif.text = buildExifText(state)
        binding.aboutAuthor.text = getString(
            R.string.about, state.data.user.username, state.data.user.bio ?: "N/A"
        )
        binding.downloadsCount.text =getString(R.string.download, state.data.downloads, state.data.downloads)

    }

    private fun showNoLocationDataSnackbar() {
        Snackbar.make(
            binding.myCoordinatorLayout,
            "getString(R.string.no_location)",
            Snackbar.LENGTH_LONG
        ).show()
    }
    @SuppressLint("StringFormatInvalid")
    private fun buildExifText(state: DetailsState.Success): String {
        return buildString {
            append(getString(R.string.made_with, state.data.exif.make ?: "N/A"))
            append(getString(R.string.model, state.data.exif.model ?: "N/A"))
            append(getString(R.string.exposure, state.data.exif.exposureTime ?: "N/A"))
            append(getString(R.string.aperture, state.data.exif.aperture ?: "N/A"))
            append(getString(R.string.focal_length, state.data.exif.focalLength ?: "N/A"))
            append(getString(R.string.iso, state.data.exif.iso?.toString() ?: "N/A"))
        }
    }

    private fun setDownloadOnClick(url: String, downloadManager: DownloadManager) {
        binding.downloadButton.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                viewModel.startDownLoad(url, downloadManager)
            } else {
                checkPermission()
                if (enableDownloadFlag) {
                    viewModel.startDownLoad(url, downloadManager)
                }
            }
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableDownloadFlag = true
        } else {
            launcher.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    private fun showSuccessfulDownloadSnackbar(uri: Uri) {
        val mySnackbar = Snackbar.make(
            binding.myCoordinatorLayout,
            "getString(R.string.download_finished)",
            Snackbar.LENGTH_INDEFINITE
        )
        mySnackbar.setAction("R.string.open", View.OnClickListener {
            val openIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            openIntent.setDataAndType(uri, "image/jpg")
            startActivity(openIntent)
        })
        mySnackbar.show()
    }


    private fun showLocationOnMap(locationUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = locationUri
        }
        startActivity(intent)
    }

    private fun setLocationClick() {

        binding.locationButton.setOnClickListener {
            Log.d(ContentValues.TAG, "lat $lat\nlon $lon ")
            if (lat != null && lon != null) {
                //showLocationOnMap(Uri.parse("geo: $lat,$lon"))
                if (lat != 0.0 && lon != 0.0) {
                    Log.d(ContentValues.TAG, "open map")
                    showLocationOnMap(Uri.parse("geo: $lat,$lon"))
                }
            } else {
                Log.d(ContentValues.TAG, "don't open map")
                showNoLocationDataSnackbar()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(receiver)
    }


}