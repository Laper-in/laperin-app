package com.capstone.laperinapp.ui.scan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.laperinapp.R
import com.capstone.laperinapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra(EXTRA_RESULT)
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }
}