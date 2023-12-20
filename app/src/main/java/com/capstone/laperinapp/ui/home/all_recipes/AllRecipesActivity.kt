package com.capstone.laperinapp.ui.home.all_recipes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.text.style.TextAppearanceSpan
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.AllRecipesAdapter
import com.capstone.laperinapp.adapter.StaggeredItemDecoration
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.databinding.ActivityAllRecipesBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.detail.DetailActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.CornerFamily

class AllRecipesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllRecipesBinding

    private val viewModel by viewModels<AllRecipesViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val adapter = AllRecipesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupCategory()
        setupRV()
    }

    private fun setupRV() {
        binding.rvRecipes.adapter = adapter
        binding.rvRecipes.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvRecipes.addItemDecoration(StaggeredItemDecoration(50))

        viewModel.getAllRecipesByCategory().observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnClickCallback(object : AllRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(
                data: DataItemRecipes,
                holder: AllRecipesAdapter.ViewHolder
            ) {
                holder.itemView.setOnClickListener {
                    val intent = Intent(this@AllRecipesActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DATA, data.id)
                    startActivity(intent)
                }
            }
        })
    }

    private fun setupCategory() {
        val chipGroup: ChipGroup = binding.chipCategory
        val chipCategory = resources.getStringArray(R.array.category_name)

        for (category in chipCategory) {
            val chip = Chip(this)
            chip.shapeAppearanceModel = chip.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 50f)
                .build()
            chip.text = category
            chip.isCheckable = true
            if (category == "All") {
                chip.isChecked = true
                chip.setTextColor(ContextCompat.getColorStateList(this, R.color.orange))
                chip.chipBackgroundColor = resources.getColorStateList(R.color.light_orange)
                viewModel.searchLiveData.value = category
            }

            chip.setOnCheckedChangeListener() { buttonView, isChecked ->
                if (isChecked) {
                    buttonView.setTextColor(
                        ContextCompat.getColorStateList(
                            this,
                            R.color.orange
                        )
                    )
                    chip.chipBackgroundColor = resources.getColorStateList(R.color.light_orange)
                    viewModel.searchLiveData.value = category
                } else {
                    buttonView.setTextColor(
                        ContextCompat.getColorStateList(
                            this,
                            R.color.black
                        )
                    )
                    chip.chipBackgroundColor = resources.getColorStateList(R.color.transparent)
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.appBar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val title = "Resep Populer"
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
}