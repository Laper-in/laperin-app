package com.capstone.laperinapp.ui.profile.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DataEditProfile
import com.capstone.laperinapp.data.response.DataUser
import com.capstone.laperinapp.databinding.ActivitySettingBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.login.LoginActivity
import com.capstone.laperinapp.ui.profile.editProfile.EditProfilActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val viewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        getData()
        changeTheme()
        binding.btnLogout.setOnClickListener { onClickLogout() }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun changeTheme() {
        binding.btnDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnDarkMode.isChecked = false
            }
        }
    }

    private fun onClickEdit(data: DataUser) {

        val user = DataEditProfile(
            data.id,
            data.username,
            data.email,
            data.fullname,
            data.telephone,
            data.alamat
        )

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditProfilActivity::class.java)
            intent.putExtra(EditProfilActivity.EXTRA_PROFILE, user)
            startActivity(intent)
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Akun"
        supportActionBar?.setHomeAsUpIndicator(com.capstone.laperinapp.R.drawable.ic_back)
    }

    private fun onClickLogout() {
        viewModel.logout()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun getData() {
        viewModel.getDetailUser(getId()).observe(this) { result ->
            when (result) {
                is com.capstone.laperinapp.helper.Result.Success -> {
                    showLoading(false)
                    setupData(result.data)
                    onClickEdit(result.data)
                }

                is com.capstone.laperinapp.helper.Result.Error -> {
                    showLoading(false)
                }

                is com.capstone.laperinapp.helper.Result.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.progressBar.visibility = android.view.View.GONE
        }
    }

    private fun getId(): String {
        val pref = UserPreference.getInstance(dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        return JWTUtils.getId(token)
    }

    private fun setupData(data: DataUser) {
        binding.apply {
            tvUsername.text = data.username
            tvEmail.text = data.email
            tvFullname.text = data.fullname
            tvAlamat.text = data.alamat
            tvNoTelp.text = data.telephone.toString()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}