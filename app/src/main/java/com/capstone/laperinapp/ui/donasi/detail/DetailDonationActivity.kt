package com.capstone.laperinapp.ui.donasi.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.data.response.DonationsItem
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

    @Suppress("DEPRECATION")
    private fun getData() {
        val data = intent.getParcelableExtra<Parcelable>(EXTRA_DATA)
        if (data is DonationsItem) {
            setData(data)
        } else {
            Toast.makeText(
                this,
                "Invalid data type received",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun setData(data: DonationsItem) {
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