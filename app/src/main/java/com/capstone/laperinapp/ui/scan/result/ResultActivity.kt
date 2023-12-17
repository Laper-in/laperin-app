package com.capstone.laperinapp.ui.scan.result

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.laperinapp.adapter.ScanResultAdapter
import com.capstone.laperinapp.adapter.SearchAdapter
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.capstone.laperinapp.databinding.ActivityResultBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.ModalBottomSheetDialog

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
        viewModel.searchStringLiveData.value = "a"
        binding.searchBar.addTextChangedListener { text ->
            viewModel.searchStringLiveData.value = text.toString()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                alertDialog()
            }

        })
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Hasil Scan"
        supportActionBar?.setHomeAsUpIndicator(com.capstone.laperinapp.R.drawable.ic_back)
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
        val decorationItem = DividerItemDecoration(this, linearLayoutManager.orientation)
        binding.rvIngredients.addItemDecoration(decorationItem)

        searchAdapter.setOnClickCallback(object : SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: IngredientItem, holder: SearchAdapter.MyViewHolder) {
                AlertDialog.Builder(this@ResultActivity)
                    .setTitle("Tambahkan ke list")
                    .setMessage("Ingin menambahkan ${data.name} ke list?")
                    .setPositiveButton("Tambahkan") { dialog, _ ->
                        viewModel.insertIngredient(ScanResult(0, data.name))
                    }
                    .setNegativeButton("Batal") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        })
    }

    private fun alertDialog() {
        AlertDialog.Builder(this@ResultActivity)
            .setTitle("Apakah anda yakin ingin keluar ?")
            .setMessage("Pilihan anda akan menghapus semua data yang telah anda scan")
            .setPositiveButton("Keluar") { dialog, _ ->
                viewModel.deleteAllResult()
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