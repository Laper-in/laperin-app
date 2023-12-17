package com.capstone.laperinapp.ui.donasi.saya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.capstone.laperinapp.R
import com.capstone.laperinapp.databinding.ActivityDonasiSayaBinding
import com.capstone.laperinapp.helper.ViewModelFactory

class DonasiSayaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonasiSayaBinding
    private val viewModel by viewModels<DonasiSayaViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonasiSayaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Donasi Saya"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}