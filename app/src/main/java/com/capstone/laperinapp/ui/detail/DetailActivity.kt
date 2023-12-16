package com.capstone.laperinapp.ui.detail

import android.annotation.SuppressLint
import android.nfc.NfcAdapter.EXTRA_DATA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.response.DataDetailRecipes
import com.capstone.laperinapp.databinding.ActivityDetailBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configToolbar()
        val id = intent.getStringExtra(EXTRA_DATA)
        getData(id)

    }

    private fun configToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle("")
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)

    }

    private fun getData(id: String?) {
        if (id != null) {
            viewModel.getRecipeById(id).observe(this){ result ->
                when(result){
                    is Result.Success -> {
                        showLoading(false)
                        setupData(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "detailError : ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
    }

    private fun setupData(data: DataDetailRecipes) {
        Glide.with(this)
            .load(data.image)
            .into(binding.imgDetail)
        binding.apply {
            tvTitle.text = data.name
            tvKategori.text = data.category
            tvIngredients.text = data.ingredient
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.progressBar.visibility = android.view.View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}