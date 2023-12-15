package com.capstone.laperinapp.ui.scan.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.adapter.SearchAdapter
import com.capstone.laperinapp.data.response.IngredientsItem
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.capstone.laperinapp.databinding.ActivitySearchIngredientBinding
import com.capstone.laperinapp.helper.ViewModelFactory

class SearchIngredientActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchIngredientBinding

    private lateinit var adapter: SearchAdapter

    private val viewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchIngredientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRV()
        setupData()
        setupSearch()
    }

    private fun setupData() {
        viewModel.getAllIngredients().observe(this) { ingredients ->
            if (ingredients != null) {
                adapter.submitData(lifecycle, ingredients)
            }
        }
    }

    private fun setupSearch() {
        binding.etSearchIngredient.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
        val searchView = binding.etSearchIngredient as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })
    }



    private fun setupRV() {
        adapter = SearchAdapter()
        binding.rvSearchIngredient.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvSearchIngredient.layoutManager = linearLayoutManager
        val decorationItem = DividerItemDecoration(this, linearLayoutManager.orientation)
        binding.rvSearchIngredient.addItemDecoration(decorationItem)

        adapter.setOnClickCallback(object : SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: IngredientsItem, holder: SearchAdapter.MyViewHolder) {
                AlertDialog.Builder(this@SearchIngredientActivity)
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
}