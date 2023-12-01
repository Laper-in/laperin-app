package com.capstone.laperinapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.btDaftarLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.apply {
            buttonLogin = btMasukLogin
            buttonLogin.isEnabled = false

            edEmailLogin.addTextChangedListener(textWatcher)
            edPasswordLogin.addTextChangedListener(textWatcher)
        }

        binding.btMasukLogin.setOnClickListener {
            val email = binding.edEmailLogin.text.toString()
            val password = binding.edPasswordLogin.text.toString()

            viewModel.setLogin(email,password)
        }

        observeLoginResult()
    }

    private fun observeLoginResult() {
        viewModel.loginResult.observe(this, { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    viewModel.isLoading.observe(this) { isLoading ->
                        showLoading(isLoading)
                    }
                }
            }
        })
    }
    private fun saveSession(user: UserModel) {
        viewModel.saveSession(user)
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
