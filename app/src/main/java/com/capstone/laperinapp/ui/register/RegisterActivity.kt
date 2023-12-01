package com.capstone.laperinapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.capstone.laperinapp.helper.Result
import androidx.activity.viewModels
import com.capstone.laperinapp.ui.customView.ButtonRegister
import com.capstone.laperinapp.databinding.ActivityRegisterBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.login.LoginActivity


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
            edNamaLengkap.addTextChangedListener(textWatcher)
            edPasswordRegister.addTextChangedListener(textWatcher)
            edUlangPassword.addTextChangedListener(textWatcher)
            edTtl.addTextChangedListener(textWatcher)
            edAlamatLengkap.addTextChangedListener(textWatcher)
        }

        binding.btRegister.setOnClickListener { onClickRegister() }
    }

    private fun onClickRegister() {
        val username = binding.edUsername.text.toString()
        val email = binding.edNamaLengkap.text.toString()
        val fullname = binding.edEmailRegister.text.toString()
        val password = binding.edPasswordRegister.text.toString()
        val alamat = binding.edAlamatLengkap.text.toString()
        val telephone = binding.edTtl.text.toString().toInt()

        Toast.makeText(this, "clicked : $username", Toast.LENGTH_SHORT).show()

        observeRegistrationResult(username, email, fullname, password, "", alamat, telephone)
    }

    private fun observeRegistrationResult(uername: String, email: String, fullname: String, password: String, picture: String, alamat: String, telephone: Int) {
        viewModel.registerUser(uername, email, fullname, password, picture, alamat, telephone).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
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
            val username = edUsername.text.toString()
            val email = edEmailRegister.text.toString()
            val namaLengkap = edNamaLengkap.text.toString()
            val password = edPasswordRegister.text.toString()
            val ulangPassword = edUlangPassword.text.toString()
            val ttl = edTtl.text.toString()
            val alamatLengkap = edAlamatLengkap.text.toString()

            val isUsernameValid = username.length >= 5

            buttonRegister.isEnabled =
                isUsernameValid && email.isNotEmpty() && namaLengkap.isNotEmpty() &&
                        password.isNotEmpty() && ulangPassword.isNotEmpty() && ttl.isNotEmpty() && alamatLengkap.isNotEmpty()
        }
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
}
