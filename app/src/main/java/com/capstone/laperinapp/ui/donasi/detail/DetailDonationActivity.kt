package com.capstone.laperinapp.ui.donasi.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.data.response.UserDetailResponse
import com.capstone.laperinapp.databinding.ActivityDetailDonationBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.helper.formatTanggal
import com.capstone.laperinapp.helper.meterToKilometer
import com.capstone.laperinapp.ui.preview.PreviewImageActivity


class DetailDonationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDonationBinding

    private val viewModel by viewModels<DetailDonationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var isMe = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isMe = intent.getBooleanExtra(EXTRA_SPECIAL, false)

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
                    getDataUser(result.data)
                    onClickDetail(result.data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onClickDetail(data: DataItemDonation) {
        binding.imgDetail.setOnClickListener {
            val intent = Intent(this, PreviewImageActivity::class.java)
            intent.putExtra(PreviewImageActivity.EXTRA_IMAGE, data.image)
            intent.putExtra(PreviewImageActivity.EXTRA_TITLE, data.name)
            startActivity(intent)
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
        binding.tvNamePengirim.text = data.fullname
        binding.tvItemJumlah.text = "Jumlah : ${data.total}"
        binding.tvItemTanggal.text = "Waktu : ${formatTanggal(data.createdAt)}"
        binding.tvItemJenis.text = "Jenis : ${data.category}"
        Log.d(TAG, "setupData: ${data.telephone}")
    }

    private fun getDataUser(data: DataItemDonation) {
        viewModel.getUser().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    if (isMe){
                        binding.linearLayout7.visibility = android.view.View.GONE
                        binding.btnSelesaiDiambil.setOnClickListener {
                            onClickSelesai(data)
                        }
                    } else {
                        binding.btnSelesaiDiambil.text = "Ambil Barang"
                        binding.btnSelesaiDiambil.setOnClickListener {
                            onClickAmbil(data, result.data)
                        }
                    }
                }

                else -> false
            }
        }
    }

    private fun onClickAmbil(data: DataItemDonation, dataUser: UserDetailResponse) {
        val phone = data.telephone
        Log.d(TAG, "onClickAmbil: $phone")
        val message = "Halo, ${data.fullname}\nSaya ingin mengambil ${data.name}. Apakah item ini masih tersedia ?"

        val url = "https://api.whatsapp.com/send?phone=$phone&text=$message"
        try {
            val pm: PackageManager = packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(
                this,
                "Whatsapp app not installed in your phone",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    private fun onClickSelesai(data: DataItemDonation) {
        viewModel.selesaikanDonasi(data.idDonation).observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, "Donasi selesai diambil", Toast.LENGTH_SHORT).show()
                    finish()
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
        const val TAG = "DetailDonationActivity"
    }
}