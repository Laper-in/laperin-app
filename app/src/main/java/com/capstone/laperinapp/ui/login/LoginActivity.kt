package com.capstone.laperinapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.capstone.laperinapp.R
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.databinding.ActivityLoginBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.MainActivity
import com.capstone.laperinapp.ui.customView.ButtonLogin
import com.capstone.laperinapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var buttonLogin: ButtonLogin
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btDaftarLogin.setOnClickListener { onClickRegister() }

        binding.apply {
            buttonLogin = btMasukLogin
            buttonLogin.isEnabled = false

            edEmailLogin.addTextChangedListener(textWatcher)
            edPasswordLogin.addTextChangedListener(textWatcher)

            icPasswordToggle.setOnClickListener { togglePasswordVisibility() }
        }

        binding.btMasukLogin.setOnClickListener { onClickLogin() }

    }

    private fun onClickLogin() {
        val username = binding.edEmailLogin.text.toString().trim()
        val password = binding.edPasswordLogin.text.toString().trim()
        if(username.contains(" ")) {
            Toast.makeText(this, "Username tidak boleh mengandung spasi", Toast.LENGTH_SHORT).show()
        } else if(password.contains(" ")) {
            Toast.makeText(this, "Password tidak boleh mengandung spasi", Toast.LENGTH_SHORT).show()
        } else {
            if (isPasswordValid(password)) {
                observeLoginResult(username, password)
            } else {
                Toast.makeText(this, "Password harus memiliki panjang minimal 6 karakter", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }


    private fun onClickRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    private fun observeLoginResult(username: String, password: String) {
        viewModel.setLogin(username, password).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(false)
                    val token = result.data.accessToken
                    val user = UserModel(username, token, true)
                    viewModel.saveSession(user)
                    ViewModelFactory.clearInstance()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
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

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkFieldsForEmptyValues()
        }
        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun togglePasswordVisibility() {
        val passwordEditText = binding.edPasswordLogin
        val passwordToggle = binding.icPasswordToggle
        if (passwordEditText.inputType == 129) {
            passwordEditText.inputType = 1
            passwordToggle.setImageResource(R.drawable.ic_password_visible)
        } else {
            passwordEditText.inputType = 129
            passwordToggle.setImageResource(R.drawable.ic_password)
        }
        passwordEditText.setSelection(passwordEditText.text!!.length)
    }

    private fun checkFieldsForEmptyValues() {
        binding.apply {
            val email = edEmailLogin.text.toString()
            val password = edPasswordLogin.text.toString()

            buttonLogin.isEnabled = email.isNotEmpty() && password.isNotEmpty()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        runOnUiThread {
            if (isLoading) {
                binding.porgressBar.visibility = View.VISIBLE
            } else {
                binding.porgressBar.visibility = View.GONE
            }
        }
    }
}
