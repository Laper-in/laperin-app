package com.capstone.laperinapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.capstone.laperinapp.helper.Result
import androidx.activity.viewModels
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.data.response.AuthResponse
import com.capstone.laperinapp.ui.customView.ButtonRegister
import com.capstone.laperinapp.databinding.ActivityRegisterBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.login.LoginActivity
import com.capstone.laperinapp.ui.welcome.WelcomeActivity


class RegisterActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var buttonRegister: ButtonRegister
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonRegister = btRegister
            buttonRegister.isEnabled = false
            edUsername.addTextChangedListener(textWatcher)
            edEmailRegister.addTextChangedListener(textWatcher)
            edPasswordRegister.addTextChangedListener(textWatcher)
            edUlangPassword.addTextChangedListener(textWatcher)
        }

        binding.btMasukToLogin.setOnClickListener { onClickLogin() }
        binding.btRegister.setOnClickListener { onClickRegister() }

        onClickCheckPassowrd()
    }

    private fun onClickLogin() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    private fun onClickCheckPassowrd() {
        binding.icPasswordToggle.setOnClickListener {
            togglePasswordVisibility(binding.edPasswordRegister, binding.icPasswordToggle)
        }

        binding.icPasswordToggle1.setOnClickListener {
            togglePasswordVisibility(binding.edUlangPassword, binding.icPasswordToggle1)
        }

    }

    private fun onClickRegister() {

        val username = binding.edUsername.text.toString().trim()
        val email = binding.edEmailRegister.text.toString()
        val password = binding.edPasswordRegister.text.toString().trim()
        val ulangPassword = binding.edUlangPassword.text.toString()

        if (username.contains(" ")) {

            Toast.makeText(this, "Username tidak boleh mengandung spasi", Toast.LENGTH_SHORT).show()
        } else if(password.contains(" ")) {

            Toast.makeText(this, "Password tidak boleh mengandung spasi", Toast.LENGTH_SHORT).show()
        } else {
            if (ulangPassword == password) {
                if (isPasswordValid()) {
                    observeRegistrationResult(username, email, password)
                } else {
                    Toast.makeText(this, "Password tidak memenuhi kriteria", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun isPasswordValid(): Boolean {
        val password = binding.edPasswordRegister.text.toString()
        return password.length > 5
    }


    private fun observeRegistrationResult(username: String, email: String, password: String) {
        viewModel.registerUser(username, email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                    saveSession(username, result.data)
                    ViewModelFactory.clearInstance()
                    finish()
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    Log.i("Regis", "observeRegistrationResult: ${result.error}")
                }
            }
        }
    }

    private fun saveSession(username: String, data: AuthResponse) {
        val user = UserModel(username, data.accessToken, true)
        viewModel.saveSession(user)
    }


    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkFieldsForEmptyValues()
        }
        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun checkFieldsForEmptyValues() {
        binding.apply {
            val username = edUsername.text.toString()
            val email = edEmailRegister.text.toString()
            val password = edPasswordRegister.text.toString()
            val ulangPassword = edUlangPassword.text.toString()

            val isUsernameValid = username.length >= 5

            buttonRegister.isEnabled =
                isUsernameValid && email.isNotEmpty() &&
                        password.isNotEmpty() && ulangPassword.isNotEmpty()

        }
    }

    private fun togglePasswordVisibility(passwordEditText: EditText, passwordToggle: ImageView) {
        if (passwordEditText.inputType == 129) {
            passwordEditText.inputType = 1
            passwordToggle.setImageResource(R.drawable.ic_password_visible)
        } else {
            passwordEditText.inputType = 129
            passwordToggle.setImageResource(R.drawable.ic_password)
        }
        passwordEditText.setSelection(passwordEditText.text.length)
    }


    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}
