package com.capstone.laperinapp.ui.scan.recommendation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.text.style.TextAppearanceSpan
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.RecommendationRecipesAdapter
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.databinding.ActivityRecommendationBinding
import com.capstone.laperinapp.databinding.ActivitySearchBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.detail.DetailActivity

class RecommendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecommendationBinding

    private val viewModel by viewModels<RecommendationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val adapter = RecommendationRecipesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupToolbar()
        setupRV()
    }

    private fun setupData() {
        viewModel.getAllResult().observe(this) {
            val result = it.joinToString(",")
            Log.i(TAG, "setupData: $result")
            viewModel.searchLiveData.value = result
        }
    }

    private fun setupRV() {
        binding.rvRecipes.adapter = adapter
        binding.rvRecipes.layoutManager = LinearLayoutManager(this)

        viewModel.searchRecommendation().observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnClickCallback(object : RecommendationRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemRecipes) {
                val intent = Intent(this@RecommendationActivity, DetailActivity::class.java)
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

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val TAG = "RecommendationActivity"
    }
}