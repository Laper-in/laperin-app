package com.capstone.laperinapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.capstone.laperinapp.MainActivity
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.databinding.ActivityLoginBinding
import com.capstone.laperinapp.helper.ViewModelFactory
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
        }

        binding.btMasukLogin.setOnClickListener { onClickLogin() }

    }

    private fun onClickLogin() {
        val email = binding.edEmailLogin.text.toString()
        val password = binding.edPasswordLogin.text.toString()

        observeLoginResult(email, password)
    }

    private fun onClickRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    private fun observeLoginResult(email: String, password: String) {
        viewModel.setLogin(email, password).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(false)
                    val token = result.data.token
                    val user = UserModel(email, token, true)
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
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            checkFieldsForEmptyValues()
        }

        override fun afterTextChanged(s: Editable?) {
            // Do nothing
        }
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
