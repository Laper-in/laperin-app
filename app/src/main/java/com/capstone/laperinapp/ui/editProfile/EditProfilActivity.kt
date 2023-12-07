package com.capstone.laperinapp.ui.editProfile

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.databinding.ActivityEditProfilBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.editProfile.picture.ButtonSheetPicture
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EditProfilActivity : AppCompatActivity() {

    private val viewModel by viewModels<EditViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding : ActivityEditProfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibBack.setOnClickListener {
            finish()
        }

        val pref = UserPreference.getInstance(dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val id = JWTUtils.getId(token)
        val password = binding.edEditPassword.text.toString()
        val email = user.email
        binding.edEditEmail.setText(email)
        val fullname = binding.edEditFullname.text.toString()
        val picture = binding.btnSelectImage.setOnClickListener {onClickKamera()}
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
        binding.btSimpan.setOnClickListener {
            updateDataUser(id, password, email, fullname, picture.toString(), alamat, telephone)
        }
    }
    private fun updateDataUser(id:String,password: String,email: String,fullname :String ,picture :String,alamat :String,telephone :Int) {
        viewModel.updateUser(id, password, email, fullname, picture, alamat, telephone).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    dataUser(result.data)
                    Toast.makeText(this, "Edit Berhasil", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun dataUser(data: DetailUserResponse) {
        binding.edEditEmail.setText(data.email)
        binding.edEditPassword.setText(data.password)
        binding.edEditFullname.setText(data.fullname)
        binding.edEditAlamat.setText(data.alamat)
        binding.edEditTelephone.setText(data.telephone.toString())
        binding.btnSelectImage.setOnClickListener {
            Glide.with(this)
                .load(data.picture)
                .into(binding.ivProfileImage)
        }
    }
    private fun allPermisionGranted() = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSIONS
    ) == PackageManager.PERMISSION_GRANTED


    private fun onClickKamera() {
        if (!allPermisionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            val modal = ButtonSheetPicture()
            supportFragmentManager.let { modal.show(it, ButtonSheetPicture.TAG) }
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
        const val EXTRA_PROFILE = "EditProfileActivity"
    }
}