package com.capstone.laperinapp.ui.donasi.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.databinding.ActivityDetailDonationBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.helper.meterToKilometer

class DetailDonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDonationBinding

    private val viewModel by viewModels<DetailDonationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configToolbar()
        getData()
    }

    private fun getData() {
        val id = intent.getStringExtra(EXTRA_DATA)
        viewModel.getDetailDonation(id.toString()).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    setupData(result.data)

                }

                is Result.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupData(data: DataItemDonation) {
        val distance = intent.getStringExtra(EXTRA_DISTANCE)!!.toDouble()
        if (distance < 1000) {
            binding.tvItemDistance.text = "$distance m"
        } else {
            val kilometer = meterToKilometer(distance)
            binding.tvItemDistance.text = kilometer
        }

        Glide.with(this)
            .load(data.image)
            .into(binding.imgDetail)

        binding.tvTitle.text = data.name
        binding.tvDeskripsi.text = data.description
        binding.tvItemJenis.text = "Jenis : ${data.category}"
        binding.tvItemJumlah.text = "Jumlah : ${data.total}"

        val isMe = intent.getBooleanExtra(EXTRA_SPECIAL, false)

        if (isMe){
            binding.linearLayout7.visibility = android.view.View.GONE
            binding.btnSelesaiDiambil.visibility = android.view.View.VISIBLE
            binding.btnSelesaiDiambil.setOnClickListener {
                onClickSelesai(data)
            }
        } else {
            binding.btnSelesaiDiambil.visibility = android.view.View.GONE
        }
    }

    private fun onClickSelesai(data: DataItemDonation) {
        viewModel.selesaikanDonasi(data.idDonation).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.btnSelesaiDiambil.isEnabled = false
                    binding.btnSelesaiDiambil.text = "Selesai"
                    true
                }
                else -> false

            }
        }
    }

    private fun configToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_solid)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DISTANCE = "extra_distance"
        const val EXTRA_SPECIAL = "extra_special"
    }
}