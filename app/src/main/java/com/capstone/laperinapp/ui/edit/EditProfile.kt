package com.capstone.laperinapp.ui.edit

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.databinding.ActivityEditProfileBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.ui.setting.SettingActivity
import com.capstone.laperinapp.ui.setting.SettingActivity.Companion.EXTRA_UPDATED_USER

class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSimpanEdit.setOnClickListener {
            onClickRegister()
        }
        binding.ibBackEdit.setOnClickListener {
            startActivity(Intent(this@EditProfile, SettingActivity::class.java))
            finish()
        }
    }
    private fun onClickRegister() {
        val pref = UserPreference.getInstance(this.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val id =  JWTUtils.getId(token)
        val name = binding.edEditFullname.text.toString()
        val email = binding.edEditEmail.text.toString()
        val password = binding.edEditPassword.text.toString()

        editDataUser(id, name, email, password)
    }

    private fun editDataUser(id: String, name: String, email: String, password: String) {
        viewModel.editDataUser(id, name, email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Edit Berhasil", Toast.LENGTH_SHORT).show()
                    handleEditSuccess(result.data)
                }

                is Result.Error -> {
                    showLoading(false)
                    val errorMessage = result.error
                    handleEditError(errorMessage)
                }
            }
        }
    }
    private fun handleEditSuccess(updatedUser: DetailUserResponse) {
        binding.edEditEmail.setText(updatedUser.email)
        val fullname = updatedUser.fullname
        if (fullname != null) {
            binding.edEditFullname.setText(fullname)
        }

        binding.edEditAlamat.setText(updatedUser.alamat)
        binding.edEditTelephone.setText(updatedUser.telephone.toString())
        binding.edEditPassword.setText(updatedUser.password)
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_UPDATED_USER, updatedUser)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
    private fun handleEditError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) {
                binding.progresBarEditProfile.visibility = View.VISIBLE
            } else {
                binding.progresBarEditProfile.visibility = View.GONE
            }
        }
    }
}
