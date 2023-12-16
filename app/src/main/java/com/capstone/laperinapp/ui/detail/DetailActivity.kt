package com.capstone.laperinapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.favorite.entity.Favorite
import com.capstone.laperinapp.data.response.DataDetailRecipes
import com.capstone.laperinapp.databinding.ActivityDetailBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var recipesFavorit: Favorite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configToolbar()

        val id = intent.getStringExtra(EXTRA_DATA)
        getData(id)

        viewModel.findFavorite(id ?: "") {
            binding.btFavorite.setColorFilter(ContextCompat.getColor(this, R.color.primary))
        }
    }

    private fun configToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    private fun getData(id: String?) {
        if (id != null) {
            viewModel.getRecipeById(id).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        setupData(result.data)
                        onClickFavorite(result.data)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "detailError: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
    }

    private fun onClickFavorite(data: DataDetailRecipes) {
        var isInFavorite = false
        recipesFavorit = Favorite(
            data.id,
            data.image,
            data.name,
            data.category,
            data.ingredient,
            "",
            ""
        )

        viewModel.getAllFavorite().observe(this) { favorite ->
            if (favorite != null) {
                isInFavorite = favorite.any { it.id == data.id }
                if (isInFavorite) {
                    binding.btFavorite.setColorFilter(ContextCompat.getColor(this, R.color.primary))
                } else {
                    binding.btFavorite.setColorFilter(ContextCompat.getColor(this, R.color.white))
                }
            }
        }

        binding.btFavorite.setOnClickListener {
            if (isInFavorite) {
                viewModel.deleteFavorite(recipesFavorit)
                Toast.makeText(this, "Recepi ${data.name} dihapus dari favorit", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertFavorite(recipesFavorit)
                Toast.makeText(this, "Recepi ${data.name} ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
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
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
