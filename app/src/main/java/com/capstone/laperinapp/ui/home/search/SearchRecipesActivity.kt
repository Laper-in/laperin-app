package com.capstone.laperinapp.ui.home.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.SearchRecipesAdapter
import com.capstone.laperinapp.data.response.RecipesItem
import com.capstone.laperinapp.databinding.ActivitySearchRecipesBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.detail.DetailActivity

class SearchRecipesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchRecipesBinding
    private val viewModel by viewModels<SearchRecipesViewModel>(){
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: SearchRecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupRV()

        viewModel.searchQuery.value = "a"
        binding.etSearchRecipes.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        binding.etSearchRecipes.addTextChangedListener { text ->
            viewModel.searchQuery.value = text.toString()
        }
    }

    private fun setupData() {
        viewModel.getRecipesByName().observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    private fun setupRV() {
        adapter = SearchRecipesAdapter()
        binding.rvSearchRecipes.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        binding.rvSearchRecipes.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : SearchRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RecipesItem) {
                val intent = Intent(this@SearchRecipesActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, data.id)
                startActivity(intent)
            }
        })
    }
}