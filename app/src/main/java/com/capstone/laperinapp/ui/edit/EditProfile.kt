package com.capstone.laperinapp.ui.edit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.databinding.ActivityEditProfileBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.ui.edit.picture.ButtonSheetPicture
import com.capstone.laperinapp.ui.edit.picture.EditPictureActivity
import com.capstone.laperinapp.ui.edit.picture.OnImageSelectedListener
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EditProfile : AppCompatActivity(), OnImageSelectedListener {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var selectedImageUri: Uri? = null
    private var isDataComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibBack.setOnClickListener { finish() }
        binding.edEditFullname.addTextChangedListener {
            checkDataCompleteness()
        }

        binding.edEditAlamat.addTextChangedListener {
            checkDataCompleteness()
        }

        binding.edEditTelephone.addTextChangedListener {
            checkDataCompleteness()
        }


        binding.btnSelectImage.setOnClickListener { onClickImportPicture() }
        binding.btSimpan.setOnClickListener { fixEditUser()}
    }

    private fun fixEditUser() {
        if (isDataComplete) {
            val pref = UserPreference.getInstance(dataStore)
            val user = runBlocking { pref.getSession().first() }
            val token = user.token
            val id = JWTUtils.getId(token)
            val fullname = binding.edEditFullname.text.toString()
            val picture = selectedImageUri.toString()
            val alamat = binding.edEditAlamat.text.toString()
            val telephoneString = binding.edEditTelephone.text.toString()
            val telephone = if (telephoneString.isNotBlank()) {
                val formattedTelephone = if (telephoneString.startsWith("62")) {
                    telephoneString
                } else {
                    "62$telephoneString"
                }
                formattedTelephone.toInt()
            } else {
                0
            }
            editDataUser(id, fullname, picture, alamat, telephone)
        } else {
            Toast.makeText(this, "Harap lengkapi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editDataUser(id: String, fullname: String, picture: String, alamat: String, telephone: Int) {
        viewModel.editDataUser(id, fullname, picture, alamat, telephone).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    Toast.makeText(this, "Edit Berhasil", Toast.LENGTH_SHORT).show()
                    finish()
                    showLoading(false)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private  fun dataUser(data : DetailUserResponse) {}

    private fun checkDataCompleteness() {
        val fullname = binding.edEditFullname.text.toString()
        val alamat = binding.edEditAlamat.text.toString()
        val telephone = binding.edEditTelephone.text.toString()

        isDataComplete = fullname.isNotEmpty() && alamat.isNotEmpty() && telephone.isNotEmpty() && selectedImageUri != null
    }
    private fun allPermisionGranted() = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSIONS
    ) == PackageManager.PERMISSION_GRANTED


    private fun onClickImportPicture() {
        if (!allPermisionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            val modal = ButtonSheetPicture()
            modal.show(supportFragmentManager, ButtonSheetPicture.TAG)
        }
    }

    override fun onImageSelected(uri: Uri) {
        selectedImageUri = uri
        checkDataCompleteness()
        val intent = Intent(this@EditProfile, EditPictureActivity::class.java)
        intent.putExtra(EditPictureActivity.EXTRA_URI, uri.toString())
        startActivityForResult(intent, REQUEST_EDIT_PICTURE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_PICTURE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.getStringExtra(EditProfile.EXTRA_PROFILE)
            binding.ivProfileImage.setImageURI(Uri.parse(selectedImageUri))
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permission request granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) {
                binding.progressBarEdit.visibility = View.VISIBLE
            } else {
                binding.progressBarEdit.visibility = View.GONE
            }
        }
    }
    companion object {
        private const val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA
        const val EXTRA_PROFILE = "EditProfile"
        private const val REQUEST_EDIT_PICTURE = 123
    }
}
