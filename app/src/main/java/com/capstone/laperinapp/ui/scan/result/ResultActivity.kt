package com.capstone.laperinapp.ui.scan.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.ScanResultAdapter
import com.capstone.laperinapp.adapter.SearchAdapter
import com.capstone.laperinapp.data.response.DataItemIngredient
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.capstone.laperinapp.databinding.ActivityResultBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.MainActivity
import com.capstone.laperinapp.ui.ModalBottomSheetDialog
import com.capstone.laperinapp.ui.scan.recommendation.RecommendationActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: ScanResultAdapter
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDataIngredient()
        setupRVIngredient()
        setupRVResult()
        getResult()

        binding.btnScan.setOnClickListener { onClickScan() }
        viewModel.searchStringLiveData.value = ""
        binding.searchBar.addTextChangedListener { text ->
            viewModel.searchStringLiveData.value = text.toString()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                alertDialog()
            }

        })

        binding.btnCariResep.setOnClickListener { onClickSearch() }
    }

    private fun onClickSearch() {
        if (binding.tvEmptyList.visibility == View.VISIBLE) {
            Toast.makeText(this, "Anda belum menambahkan bahan", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = "Hasil Scan"
        val spannableTitle = SpannableString(title)
        spannableTitle.setSpan(
            TextAppearanceSpan(this, R.style.textColorDonasi),
            0,
            title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        supportActionBar?.title = spannableTitle
        val backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back)
        backIcon?.let {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
            val imageSpan = ImageSpan(it, ImageSpan.ALIGN_BASELINE)
            spannableTitle.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun onClickScan() {
        val modal = ModalBottomSheetDialog()
        supportFragmentManager.let { modal.show(it, ModalBottomSheetDialog.TAG) }
    }

    private fun setupRVResult() {
        adapter = ScanResultAdapter()
        binding.rvResult.adapter = adapter
        val layoutManager = GridLayoutManager(this, 3)
        binding.rvResult.layoutManager = layoutManager
    }

    private fun getResult() {
        viewModel.getAllResult().observe(this) { result ->
            if (result != null) {
                adapter.submitList(result)
                binding.tvEmptyList.visibility = if (result.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                onClickAdd(result)
            }
        }

        adapter.setOnItemClickCallback(object : ScanResultAdapter.OnItemClickCallback {
            override fun onItemClicked(
                data: ScanResult,
                holder: ScanResultAdapter.ScanResultViewHolder
            ) {
                holder.binding.btnDelete.setOnClickListener {
                    viewModel.deleteById(data)
                }
            }
        })
    }

    private fun setupDataIngredient() {
        viewModel.getIngredientByName().observe(this) {
            searchAdapter.submitData(lifecycle, it)
        }
    }

    private fun setupRVIngredient() {
        searchAdapter = SearchAdapter()
        binding.rvIngredients.adapter = searchAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvIngredients.layoutManager = linearLayoutManager
    }

    private fun onClickAdd(result: List<ScanResult>) {
        searchAdapter.setOnClickCallback(object : SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(
                data: DataItemIngredient,
                holder: SearchAdapter.MyViewHolder
            ) {
                holder.binding.btnAdd.setOnClickListener {
                    viewModel.containsIngredient(data.name).observe(this@ResultActivity) {isExisting ->
                        AlertDialog.Builder(this@ResultActivity)
                            .setTitle("Tambahkan ke list")
                            .setMessage("Ingin menambahkan ${data.name} ke list?")
                            .setPositiveButton("Tambahkan") { dialog, _ ->
                                if (isExisting) {
                                    Toast.makeText(
                                        this@ResultActivity,
                                        "${data.name} sudah ada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.insertIngredient(ScanResult(0, data.name))
                                    Toast.makeText(
                                        this@ResultActivity,
                                        "${data.name} berhasil ditambahkan",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                            .setNegativeButton("Batal") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                }
            }
        })
    }

    private fun alertDialog() {
        AlertDialog.Builder(this@ResultActivity)
            .setTitle("Apakah anda yakin ingin keluar ?")
            .setMessage("Pilihan anda akan menghapus semua data yang telah anda scan")
            .setPositiveButton("Keluar") { dialog, _ ->
                viewModel.deleteAllResult()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        alertDialog()
        return super.onSupportNavigateUp()
    }


    companion object {
        const val EXTRA_RESULT = "extra_result"
    }
}