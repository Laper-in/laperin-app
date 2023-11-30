package com.capstone.laperinapp.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.laperinapp.R
import com.capstone.laperinapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}