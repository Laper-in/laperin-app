package com.capstone.laperinapp.ui.setting

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.text.isDigitsOnly
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.databinding.ActivitySettingBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.edit.EditProfile
import com.capstone.laperinapp.ui.login.LoginActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SettingActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewLogout.setOnClickListener { showLogoutConfirmationDialog() }
        binding.ibBackSetting.setOnClickListener { finish() }
        binding.ibEditAkun.setOnClickListener {
            startActivity(Intent(this@SettingActivity, EditProfile::class.java))
            finish()
        }
        getData()
        binding.ibEditAkun.setOnClickListener {
            val intent = Intent(this@SettingActivity, EditProfile::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE)
        }
    }

    private fun onClickLogout() {
        viewModel.logout()
        startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
        finish()
    }

    private fun getData() {
        val pref = UserPreference.getInstance(this.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val id = JWTUtils.getId(token)
        setupDataUser(id)
    }
    private fun setupDataUser(id: String?) {
        if (id != null) {
            viewModel.getUser(id).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        dataUser(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "ProfileError : ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
    }

    private fun dataUser(data: DetailUserResponse) {
        binding.tvDetailUsername.text = data.username
        binding.tvDetailEmail.text = data.email
        binding.tvDetailFullname.text = data.fullname
        binding.tvDetailAlamat.text = data.alamat
        val telephone = data.telephone.toString()
        if (telephone.isDigitsOnly()) {
            binding.tvDetailTelephone.text = telephone
        } else {
            binding.tvDetailTelephone.text = "Invalid Telephone"
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Keluar")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ -> onClickLogout() }
            .setNegativeButton("Tidak") { _, _ ->  }
            .create()
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == Activity.RESULT_OK) {
            val updatedUser = data?.getParcelableExtra<DetailUserResponse>(EXTRA_UPDATED_USER)
            if (updatedUser != null) {
                dataUser(updatedUser)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) {
                binding.progresbarSetting.visibility = View.VISIBLE
            } else {
                binding.progresbarSetting.visibility = View.GONE
            }
        }
    }
    companion object {
        const val REQUEST_CODE_EDIT_PROFILE = 1001
        const val EXTRA_UPDATED_USER = "extra_updated_user"
    }

}
