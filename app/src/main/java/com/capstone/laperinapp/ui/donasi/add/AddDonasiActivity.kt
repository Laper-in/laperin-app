package com.capstone.laperinapp.ui.donasi.add

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.databinding.ActivityAddDonasiBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.helper.reduceFileImage
import com.capstone.laperinapp.helper.uriToFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.ui.donasi.camera.ModalBottomSheetDonationDialog

class AddDonasiActivity : AppCompatActivity(), OnImageSelectedListener {

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

    override fun onImageSelected(uri: Uri) {
        showImage(uri)
    }

    private fun getData() {
        binding.btnDonasi.setOnClickListener {
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
            if (name.length >= 5) {
                sendData(id, username, name, description, category, total, latitude, longitude)
            } else {
                Toast.makeText(this@AddDonasiActivity, "Nama harus lebih dari 5 karakter", Toast.LENGTH_SHORT).show()
            }
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
        val modalBottomSheet = ModalBottomSheetDonationDialog()
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetDonationDialog.TAG)
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

        lifecycleScope.launch {
            try {
                val requestBodyUserId = id!!.toRequestBody("text/plain".toMediaType())
                val requestBodyUsername = username!!.toRequestBody("text/plain".toMediaType())
                val requestBodyName = name.toRequestBody("text/plain".toMediaType())
                val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
                val requestBodyCategory = category.toRequestBody("text/plain".toMediaType())
                val requestBodyTotal = total.toRequestBody("text/plain".toMediaType())
                val requestBodyLatitude = latitude!!.toRequestBody("text/plain".toMediaType())
                val requestBodyLongitude = longitude!!.toRequestBody("text/plain".toMediaType())

                viewModel.sendDonation(
                    requestBodyUserId,
                    requestBodyUsername,
                    requestBodyName,
                    requestBodyDescription,
                    requestBodyCategory,
                    requestBodyTotal,
                    requestBodyLongitude,
                    requestBodyLatitude,
                    createImageRequestBody()
                ).observe(this@AddDonasiActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AddDonasiActivity, "Donasi berhasil di upload", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@AddDonasiActivity, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "sendData: ${e.message.toString()}")
            }
        }
    }

    private fun createImageRequestBody(): MultipartBody.Part {
        currentImageUri?.let { uri ->
            val image = uriToFile(uri, this).reduceFileImage()
            val requestBodyImage = image.asRequestBody("image/jpeg".toMediaType())
            return MultipartBody.Part.createFormData("image", image.name, requestBodyImage)
        }
        throw IllegalStateException("Current image URI is null.")
    }

    private fun showImage(uri: Uri?) {
        currentImageUri = uri
        binding.apply {
            imgPreview.setImageURI(currentImageUri)
            imgPreview.visibility = if (currentImageUri != null) View.VISIBLE else View.GONE
            btnRemoveImage.visibility = if (currentImageUri != null) View.VISIBLE else View.GONE
            btnImage.visibility = if (currentImageUri != null) View.GONE else View.VISIBLE
            llInputFoto.visibility = if (currentImageUri != null) View.GONE else View.VISIBLE
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_ADD = "addDonasiActivity"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_URI = "extra_uri"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val REQUEST_CODE = 100
        private const val TAG = "AddDonasiActivity"
    }
}