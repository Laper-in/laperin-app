package com.capstone.laperinapp.ui.donasi.add

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.databinding.ActivityAddDonasiBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.helper.reduceFileImage
import com.capstone.laperinapp.helper.uriToFile
import com.capstone.laperinapp.ui.ModalBottomSheetDialog
import com.capstone.laperinapp.ui.donasi.camera.CameraDonationActivity
import com.capstone.laperinapp.ui.donasi.camera.ModalBottomSheetDonationDialog
import com.capstone.laperinapp.ui.donasi.camera.OnImageResultListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddDonasiActivity : AppCompatActivity(), OnImageResultListener {

    private lateinit var binding: ActivityAddDonasiBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AddDonationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDonasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

        binding.btnImage.setOnClickListener { openModal() }
        binding.btnRemoveImage.setOnClickListener { showImage(null) }

        setupToolbar()
    }

    override fun onImageResult(uri: Uri) {
        Log.d(TAG, "onImageResult: $uri")
        currentImageUri = uri
    }

    private fun getData() {
        val pref = UserPreference.getInstance(this.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val id = JWTUtils.getId(token)
        val username = JWTUtils.getUsername(token)
        val latitude = "123"
        val longitude = "123"
        val name = binding.edNama.text.toString()
        val description = binding.edDescription.text.toString()
        val category = "charity"
        val total = binding.edJumlah.text.toString()

        binding.btnDonasi.setOnClickListener {
            sendData(
                id,
                username,
                name,
                description,
                category,
                total,
                latitude,
                longitude
            )
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Tambahkan Donasi"
    }

    private fun openModal() {
        val modal = ModalBottomSheetDonationDialog()
        supportFragmentManager.let { modal.show(it, ModalBottomSheetDonationDialog.TAG) }
    }

    private fun sendData(
        id: String? = null,
        username: String? = null,
        name: String,
        description: String,
        category: String,
        total: String,
        latitude: String? = null,
        longitude: String? = null
    ) {
        currentImageUri?.let { uri ->
            val image = uriToFile(uri, this).reduceFileImage()

            val requestBodyUserId = id!!.toRequestBody("text/plain".toMediaType())
            val requestBodyUsername = username!!.toRequestBody("text/plain".toMediaType())
            val requestBodyName = name.toRequestBody("text/plain".toMediaType())
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
            val requestBodyCategory = category.toRequestBody("text/plain".toMediaType())
            val requestBodyTotal = total.toRequestBody("text/plain".toMediaType())
            val requestBodyLatitude = latitude!!.toRequestBody("text/plain".toMediaType())
            val requestBodyLongitude = longitude!!.toRequestBody("text/plain".toMediaType())
            val requestBodyImage = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData("image", image.name, requestBodyImage)

            lifecycleScope.launch {
                try {
                    viewModel.sendDonation(
                        requestBodyUserId,
                        requestBodyUsername,
                        requestBodyName,
                        requestBodyDescription,
                        requestBodyCategory,
                        requestBodyTotal,
                        requestBodyLongitude,
                        requestBodyLatitude,
                        multipartBody
                    )
                    finish()
                } catch (e: Exception) {
                    Log.e(TAG, "sendData: ${e.message.toString()}")
                }
            }

        }

    }

    private fun showImage(uri: Uri?) {
        currentImageUri = uri
        binding.apply {
            imgPreview.setImageURI(currentImageUri)
            imgPreview.visibility = if (currentImageUri != null) View.VISIBLE else View.GONE
            btnRemoveImage.visibility = if (currentImageUri != null) View.VISIBLE else View.GONE
            btnImage.visibility = if (currentImageUri != null) View.GONE else View.VISIBLE
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_URI = "extra_uri"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val REQUEST_CODE = 100
        private const val TAG = "AddDonasiActivity"
    }
}