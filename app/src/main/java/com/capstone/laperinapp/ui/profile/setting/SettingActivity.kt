package com.capstone.laperinapp.ui.profile.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.text.style.TextAppearanceSpan
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.response.DataDetailUser
import com.capstone.laperinapp.data.response.DataEditProfile
import com.capstone.laperinapp.data.response.UserDetailResponse
import com.capstone.laperinapp.databinding.ActivitySettingBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.login.LoginActivity
import com.capstone.laperinapp.ui.profile.editProfile.EditProfilActivity

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val viewModel by viewModels<SettingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var user = DataEditProfile(
        "",
        "",
        "",
        "",
        0,
        ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        getData()
        settingTheme()
        binding.btnLogout.setOnClickListener { onClickLogout() }
        val imageView = findViewById<ImageView>(R.id.btn_edit)
        val tintColorResId =
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                R.color.white
            } else {
                R.color.black
            }
        val tintColorList = ContextCompat.getColorStateList(this, tintColorResId)
        ImageViewCompat.setImageTintList(imageView, tintColorList)

        binding.btnEdit.setOnClickListener { onClickEdit() }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun settingTheme() {
        viewModel.getThemeSetting().observe(this) { isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnDarkMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnDarkMode.isChecked = false
            }
        }

        binding.btnDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    private fun onClickEdit() {
        val intent = Intent(this, EditProfilActivity::class.java)
        intent.putExtra(EditProfilActivity.EXTRA_PROFILE, user)
        startActivity(intent)
    }


    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = "Akun"
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(
            TextAppearanceSpan(this, R.style.textColorDonasi),
            0,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        supportActionBar?.title = spannableTitle
        val backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        backIcon?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BASELINE)
            spannableTitle.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun onClickLogout() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Keluar")
            .setMessage("Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.logout()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getData() {
        viewModel.getDetailUser().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(false)
                    setupData(result.data)
                    getDataUser(result.data.data)
                }

                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun getDataUser(data: DataDetailUser) {
        user = DataEditProfile(
            data.id ?: "",
            data.username ?: "",
            data.email ?: "",
            data.fullname ?: "",
            data.telephone ?: 0,
            data.alamat ?: ""
        )
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.progressBar.visibility = android.view.View.GONE
        }
    }

    private fun setupData(data: UserDetailResponse) {
        binding.apply {
            tvUsername.text = data.data.username
            tvEmail.text = data.data.email
            if (data.data.fullname == null) tvFullname.text =
                getString(R.string.nama_kosong) else tvFullname.text = data.data.fullname
            if (data.data.alamat == null) tvAlamat.text =
                getString(R.string.alamat_kosong) else tvAlamat.text = data.data.alamat
            if (data.data.telephone.toInt() == 0) tvNoTelp.text =
                getString(R.string.telephone_kosong) else tvNoTelp.text =
                data.data.telephone.toString()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG = "SettingActivity"
    }
}