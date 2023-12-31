package com.capstone.laperinapp.ui.donasi.add

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.UserDetailResponse
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

    private var latitude: String? = null
    private var longitude: String? = null
    private var username: String? = null
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDonasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

        binding.btnImage.setOnClickListener { openModal() }
        binding.btnRemoveImage.setOnClickListener { showImage(null) }

        setupSpinner()
        setupToolbar()
    }

    private fun setupSpinner() {
        val listCategory = resources.getStringArray(R.array.category_name)
        val categoryAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listCategory)
        val spinner = binding.spnJenis
        spinner.adapter = categoryAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category = listCategory[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    override fun onImageSelected(uri: Uri) {
        showImage(uri)
    }

    private fun getData() {
        val bundle = intent.extras
        if (bundle != null) {
            latitude = bundle.getString(EXTRA_LATITUDE)
            longitude = bundle.getString(EXTRA_LONGITUDE)
            username = bundle.getString(EXTRA_USERNAME)
        }
        Log.d(TAG, "getDatalocation: $latitude, $longitude, $username")
        viewModel.getUser().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.btnDonasi.setOnClickListener {
                        val name = binding.edNama.text.toString()
                        val description = binding.edDescription.text.toString()
                        val category = category!!
                        val total = binding.edJumlah.text.toString()
                        val userImage = result.data.data.image
                        val telephone = result.data.data.telephone.toString()
                        if (name.length >= 5) {
                            sendData(username, name, description, category, total, latitude, longitude, userImage, telephone)
                        } else {
                            Toast.makeText(this@AddDonasiActivity, "Judul harus lebih dari 5 karakter", Toast.LENGTH_SHORT).show()
                        }
                    }

                    Glide.with(this@AddDonasiActivity)
                        .load(result.data.data.image)
                        .circleCrop()
                        .into(binding.imgUser)
                    binding.tvUsernameProfil.text = result.data.data.fullname
                }
                else -> false
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = "Buat Donasi"
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(TextAppearanceSpan(this, R.style.textColorDonasi), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar?.title = spannableTitle
        val backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        backIcon?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BASELINE)
            spannableTitle.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun openModal() {
        val modalBottomSheet = ModalBottomSheetDonationDialog()
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheetDonationDialog.TAG)
    }

    private fun sendData(
        username: String? = null,
        name: String,
        description: String,
        category: String,
        total: String,
        latitude: String? = null,
        longitude: String? = null,
        imageUser: String? = null,
        telephone: String? = null
    ) {

        lifecycleScope.launch {
            try {
                val requestBodyUsername = username!!.toRequestBody("text/plain".toMediaType())
                val requestBodyName = name.toRequestBody("text/plain".toMediaType())
                val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
                val requestBodyCategory = category.toRequestBody("text/plain".toMediaType())
                val requestBodyTotal = total.toRequestBody("text/plain".toMediaType())
                val requestBodyLatitude = latitude!!.toRequestBody("text/plain".toMediaType())
                val requestBodyLongitude = longitude!!.toRequestBody("text/plain".toMediaType())
                val requestBodyImageUser = imageUser!!.toRequestBody("text/plain".toMediaType())
                val requestBodyTelephone = telephone!!.toRequestBody("text/plain".toMediaType())

                viewModel.sendDonation(
                    requestBodyUsername,
                    requestBodyName,
                    requestBodyDescription,
                    requestBodyCategory,
                    requestBodyTotal,
                    requestBodyLongitude,
                    requestBodyLatitude,
                    createImageRequestBody(),
                    requestBodyImageUser,
                    requestBodyTelephone
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