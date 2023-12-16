package com.capstone.laperinapp.ui.profile.editProfile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DataEditProfile
import com.capstone.laperinapp.data.response.DataUser
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.databinding.ActivityEditProfilBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.helper.formatTelpNumber
import com.capstone.laperinapp.ui.profile.editProfile.picture.ButtonSheetPicture
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

class EditProfilActivity : AppCompatActivity() {

    private val viewModel by viewModels<EditViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding : ActivityEditProfilBinding
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        getDataIntent()
        Log.d(TAG, "idProfile: $id")

        binding.btSimpan.setOnClickListener { onClickSimpan() }
    }

    private fun onClickSimpan() {
        val email = binding.edEditEmail.text.toString()
        val fullname = binding.edEditFullname.text.toString()
        val alamat = binding.edEditAlamat.text.toString()
        val telephone = binding.edEditTelephone.text.toString().trim()
        val formatedTelephone = formatTelpNumber(telephone)
        val digitOnly = formatedTelephone.replace("[^0-9]".toRegex(), "")
        val telp: Long
        if (formatedTelephone == "Tidak Valid"){
            Toast.makeText(this, "Nomor telepon tidak valid", Toast.LENGTH_SHORT).show()
            return
        } else {
            try {
                telp = digitOnly.toLong()
            } catch (e: Exception) {
                Toast.makeText(this, "Masih String", Toast.LENGTH_SHORT).show()
                return
            }
        }

        viewModel.updateUser(id, email, fullname, alamat, telp.toBigInteger()).observe(this) { result ->
            when(result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    Log.d(TAG, "resultEdit: ${result.data}")
                    Toast.makeText(this, "Berhasil mengubah data", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Edit Akun"
        supportActionBar?.setHomeAsUpIndicator(com.capstone.laperinapp.R.drawable.ic_back)
    }

    private fun getDataIntent() {
        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<DataEditProfile>(EXTRA_PROFILE, DataEditProfile::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<DataEditProfile>(EXTRA_PROFILE)
        }

        setupData(user)
    }

    private fun setupData(data: DataEditProfile?) {
        binding.apply {
            id = data?.id.toString()
            edEditUsername.setText(data?.username)
            edEditEmail.setText(data?.email)
            edEditFullname.setText(data?.fullname)
            edEditAlamat.setText(data?.alamat)
            edEditTelephone.setText(data?.telephone?.toString())
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_PROFILE = "extra_profile"
        const val TAG = "EditProfilActivity"
    }
}