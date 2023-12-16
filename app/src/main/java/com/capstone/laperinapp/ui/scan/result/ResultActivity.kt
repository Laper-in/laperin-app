package com.capstone.laperinapp.ui.scan.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.adapter.ScanResultAdapter
import com.capstone.laperinapp.adapter.SearchAdapter
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.response.IngredientsItem
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.capstone.laperinapp.databinding.ActivityResultBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.ModalBottomSheetDialog
import com.capstone.laperinapp.ui.scan.search.SearchIngredientActivity

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

        setupRVResult()
        getResult()
        setupSearch()

        binding.btnScan.setOnClickListener { onClickScan() }
    }

    private fun setupSearch() {
        binding.searchBar.setOnClickListener {
            val intent = Intent(this, SearchIngredientActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickScan() {
        val modal = ModalBottomSheetDialog()
        supportFragmentManager.let { modal.show(it, ModalBottomSheetDialog.TAG) }
    }

    private fun setupRVResult() {
        adapter = ScanResultAdapter()
        binding.rvResult.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvResult.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvResult.addItemDecoration(itemDecoration)
    }

    private fun getResult() {
        viewModel.getAllResult().observe(this) { result ->
            if (result != null) {
                adapter.submitList(result)
            }
        }

        adapter.setOnItemClickCallback(object : ScanResultAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ScanResult) {
                viewModel.deleteById(data)
            }
        })
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
    }
}