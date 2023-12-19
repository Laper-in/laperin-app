package com.capstone.laperinapp.ui.donasi.saya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.CompletedDonationAdapter
import com.capstone.laperinapp.adapter.LoadingStateAdapter
import com.capstone.laperinapp.adapter.UncompletedDonationAdapter
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.databinding.ActivityDonasiSayaBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.donasi.add.AddDonasiActivity
import com.capstone.laperinapp.ui.donasi.detail.DetailDonationActivity
import okhttp3.internal.notifyAll

class DonasiSayaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonasiSayaBinding
    private val viewModel by viewModels<DonasiSayaViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var uncompletedAdapter: UncompletedDonationAdapter
    private lateinit var completedAdapter: CompletedDonationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonasiSayaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        setupRVUncompleted()
        setupRVCompleted()
    }

    private fun setupRVCompleted() {
        completedAdapter = CompletedDonationAdapter()
        binding.rvCompleted.adapter = completedAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { completedAdapter.retry() }
        )
        binding.rvCompleted.layoutManager = LinearLayoutManager(this)

        viewModel.myCompletedDonations().observe(this) {
            completedAdapter.submitData(lifecycle, it)
        }

        completedAdapter.setOnClickCallback(object : CompletedDonationAdapter.OnItemClickCallback {
            override fun onItemClicked(
                data: DataItemDonation,
                holder: CompletedDonationAdapter.ViewHolder
            ) {
                holder.itemView.setOnClickListener {
                    val intent = Intent(this@DonasiSayaActivity, DetailDonationActivity::class.java)
                    intent.putExtra(DetailDonationActivity.EXTRA_DATA, data.idDonation)
                    intent.putExtra(DetailDonationActivity.EXTRA_DISTANCE, data.distance.toString())
                    startActivity(intent)
                }
            }
        })

        completedAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.progressBarCompleted.visibility = View.VISIBLE
            } else {
                binding.progressBarCompleted.visibility = View.GONE

                if (completedAdapter.itemCount != 0) {
                    binding.emptyCompleted.visibility = View.GONE
                } else {
                    binding.emptyCompleted.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupRVUncompleted() {
        uncompletedAdapter = UncompletedDonationAdapter()
        binding.rvUncompleted.adapter = uncompletedAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { uncompletedAdapter.retry() }
        )
        binding.rvUncompleted.layoutManager = LinearLayoutManager(this)

        viewModel.myUncompletedDonations().observe(this) {
            uncompletedAdapter.submitData(lifecycle, it)
        }

        uncompletedAdapter.setOnClickCallback(object : UncompletedDonationAdapter.OnItemClickCallback {
            override fun onItemClicked(
                data: DataItemDonation,
                holder: UncompletedDonationAdapter.ViewHolder
            ) {
                holder.binding.btnSelesaiDiambil.setOnClickListener {
                    alertDialog(data)
                }
                holder.itemView.setOnClickListener {
                    val intent = Intent(this@DonasiSayaActivity, DetailDonationActivity::class.java)
                    intent.putExtra(DetailDonationActivity.EXTRA_DATA, data.idDonation)
                    intent.putExtra(DetailDonationActivity.EXTRA_DISTANCE, data.distance.toString())
                    intent.putExtra(DetailDonationActivity.EXTRA_SPECIAL, true)
                    startActivity(intent)
                }
            }
        })

        uncompletedAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.progressBarUncompleted.visibility = android.view.View.VISIBLE
            } else {
                binding.progressBarUncompleted.visibility = android.view.View.GONE
            }
        }
    }

    private fun alertDialog(data: DataItemDonation) {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah barang sudah diambil?")
            .setPositiveButton("Sudah") { dialog, _ ->
                viewModel.selesaikanDonasi(data.idDonation).observe(this) { result ->
                    when (result) {
                        is Result.Success -> {
                            dialog.dismiss()
                            true
                        }
                        is Result.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            true
                        }
                        else -> false

                    }
                }
            }
            .setNegativeButton("Belum") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = "Donasi Saya"
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(TextAppearanceSpan(this, R.style.textColorDonasi), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar?.title = spannableTitle
        val backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        backIcon?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BASELINE)
            spannableTitle.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG = "DonasiSayaActivity"
    }
}