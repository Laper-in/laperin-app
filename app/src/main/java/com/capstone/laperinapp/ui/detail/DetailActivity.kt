package com.capstone.laperinapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.BookmarkAdapter
import com.capstone.laperinapp.adapter.CategoryAdapter
import com.capstone.laperinapp.data.room.favorite.entity.Favorite
import com.capstone.laperinapp.data.model.Category
import com.capstone.laperinapp.data.response.DataItemBookmark
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.databinding.ActivityDetailBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.helper.formatDuration

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var isInFavorite = false

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

    private fun setupCategory(category: String) {
        val data = category.split(",")
        val listCategory = ArrayList<Category>()
        for (i in data.indices) {
            listCategory.add(Category(data[i]))
        }

        val categoryAdapter = CategoryAdapter(listCategory)
        binding.rvCategory.adapter = categoryAdapter
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.layoutManager = layoutManager
    }

    private fun configToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_solid)
    }

    private fun getData(id: String?) {
        if (id != null) {
            viewModel.getRecipeById(id).observe(this) { result ->
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        setupData(result.data)
                        onClickFavorite(result.data)
                        onClickVideo(result.data.video)
                        setupCategory(result.data.category)
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

    private fun onClickVideo(urlVideo: String) {
        binding.btnVideo.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra(VideoActivity.EXTRA_DATA, urlVideo)
            startActivity(intent)
        }
    }

    private fun onClickFavorite(data: DataItemRecipes) {
        recipesFavorit = Favorite(
            data.id,
            data.image,
            data.name,
            data.description,
            data.category,
            data.ingredient,
            data.guide,
            data.video
        )

        viewModel.getAllBookmarks().observe(this) { result ->
            //TODO("ambil id")
        }

//        viewModel.getAllFavorite().observe(this) { favorite ->
//            if (favorite != null) {
//                isInFavorite = favorite.any { it.id == data.id }
//                if (isInFavorite) {
//                    binding.btFavorite.setImageResource(R.drawable.ic_bookmark_24)
//                } else {
//                    binding.btFavorite.setImageResource(R.drawable.ic_bookmark_border_24)
//                }
//            }
//        }

        binding.btFavorite.setOnClickListener {
            if (isInFavorite) {
                viewModel.deleteFavorite(recipesFavorit)
                Toast.makeText(this, "${data.name} dihapus dari favorit", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertFavorite(recipesFavorit)
                Toast.makeText(this, "${data.name} ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupData(data: DataItemRecipes) {
        Glide.with(this)
            .load(data.image)
            .into(binding.imgDetail)

        Glide.with(this)
            .load(data.image)
            .into(binding.imgThumbnail)

        val dataBahan = data.ingredient.split("\n")
        val formatedBahan = dataBahan.joinToString("\n") { "• $it" }

//        val dataTahapan = data.guide.split("\n")
//        var counter = 1
//        val formatedTahapan = dataTahapan.joinToString("\n") { "$counter. $it".also { counter++ } }

        val formatedTime = formatDuration(this, data.time)

        binding.apply {
            tvTitle.text = data.name
            tvDescription.text = data.description
            tvDurasi.text = formatedTime
            tvIngredients.text = formatedBahan
            tvSteps.text = data.guide
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
        private const val TAG = "DetailActivity"
    }
}
