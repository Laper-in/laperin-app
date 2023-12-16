package com.capstone.laperinapp.ui.donasi.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.databinding.ActivityDetailDonationBinding

class DetailDonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDonationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configToolbar()
        getData()
    }

    private fun configToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

    }

    private fun getData() {
        val data = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<ClosestDonationsItem>(EXTRA_DATA, ClosestDonationsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA) as ClosestDonationsItem?
        }

        if (data != null) {
            setData(data)
        }
    }

    private fun setData(data: ClosestDonationsItem) {
        Glide.with(this)
            .load(data.image)
            .into(binding.imgDetail)
        binding.apply {
            tvTitle.text = data.name
            tvKategori.text = data.category
            tvDescription.text = data.description
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object{
        const val EXTRA_DATA = "extra_data"
    }
}