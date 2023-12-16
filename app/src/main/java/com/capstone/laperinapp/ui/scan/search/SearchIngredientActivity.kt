package com.capstone.laperinapp.ui.scan.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.adapter.SearchAdapter
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.response.IngredientsItem
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.capstone.laperinapp.databinding.ActivitySearchIngredientBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchIngredientActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchIngredientBinding

    private lateinit var adapter: SearchAdapter

    private val viewModel by viewModels<SearchViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var currentText = "a"
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        Log.d(TAG, "currentText: $currentText")
        viewModel.submitQuery(currentText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchIngredientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRV()
        setupData()

        viewModel.searchStringLiveData.value = "a"
        binding.etSearchIngredient.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        binding.etSearchIngredient.addTextChangedListener { text ->
            viewModel.searchStringLiveData.value = text.toString()
        }
    }

    private fun setupData() {
        viewModel.getIngredientByName().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupRV() {
        adapter = SearchAdapter()
        binding.rvSearchIngredient.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this)
        binding.rvSearchIngredient.layoutManager = linearLayoutManager
        val decorationItem = DividerItemDecoration(this, linearLayoutManager.orientation)
        binding.rvSearchIngredient.addItemDecoration(decorationItem)

        adapter.setOnClickCallback(object : SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: IngredientItem, holder: SearchAdapter.MyViewHolder) {
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

    companion object {
        private const val TAG = "SearchIngredientActivity"
    }
}