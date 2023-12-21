package com.capstone.laperinapp.ui.preview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.databinding.ActivityPreviewImageBinding

class PreviewImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupData()
    }

    private fun setupData() {
        val image = intent.getStringExtra(EXTRA_IMAGE)
        Glide.with(this)
            .load(image)
            .into(binding.ivPreview)
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = intent.getStringExtra(EXTRA_TITLE)
        supportActionBar?.title = title
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_24)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_TITLE = "extra_title"
    }
}