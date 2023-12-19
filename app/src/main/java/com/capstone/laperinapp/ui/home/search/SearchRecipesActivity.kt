package com.capstone.laperinapp.ui.home.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.text.style.TextAppearanceSpan
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.LoadingStateAdapter
import com.capstone.laperinapp.adapter.SearchRecipesAdapter
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.databinding.ActivitySearchBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.detail.DetailActivity

class SearchRecipesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel by viewModels<SearchRecipesViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val searchAdapter = SearchRecipesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSearch()
        setupToolbar()
        setupRVRecipe()
    }

    private fun setupSearch() {
        binding.searchBar.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        binding.searchBar.addTextChangedListener {
            viewModel.searchLiveData.value = it.toString()
        }
    }

    private fun setupRVRecipe() {
        binding.rvRecipes.adapter = searchAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter { searchAdapter.retry() }
        )
        binding.rvRecipes.layoutManager = LinearLayoutManager(this)

        viewModel.searchByName().observe(this) {
            searchAdapter.submitData(lifecycle, it)
        }

        searchAdapter.setOnItemClickCallback(object : SearchRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemRecipes) {
                val intent = Intent(this@SearchRecipesActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, data.id)
                startActivity(intent)
            }
        })
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = "Cari resep"
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
}