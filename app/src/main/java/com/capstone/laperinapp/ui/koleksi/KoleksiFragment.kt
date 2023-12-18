package com.capstone.laperinapp.ui.koleksi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.favorite.adapter.FavoriteAdapter
import com.capstone.laperinapp.databinding.FragmentKoleksiBinding
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.customView.ChipCategory
import com.capstone.laperinapp.ui.detail.DetailActivity
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.CornerFamily

class KoleksiFragment : Fragment() {

    private var _binding: FragmentKoleksiBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<KoleksiViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var adapter : FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKoleksiBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRV()
        getData()
        setupCategory()
    }

    @Suppress("DEPRECATION")
    private fun setupCategory() {
        val chipGroup: ChipGroup = binding.chipCategory
        val chipCategory = listOf("All", "Makanan", "Minuman", "Kue", "Jajanan", "Roti", "Kopi", "Teh", "Soda", "Jus", "Smoothies", "Es", "Lainnya")

        for (category in chipCategory) {
            val chip = Chip(requireContext())
            chip.shapeAppearanceModel = chip.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 50f)
                .build()
            chip.text = category
            chip.isCheckable = true
            if (category == "All") chip.isChecked = true

            chip.setOnCheckedChangeListener() { buttonView, isChecked ->
                buttonView.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.dark_gray))
                if (isChecked) {
                    buttonView.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.orange))
                    chip.chipBackgroundColor = resources.getColorStateList(R.color.light_orange)
                    Toast.makeText(requireContext(), category, Toast.LENGTH_SHORT).show()
                } else {
                    buttonView.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.dark_gray))
                    chip.chipBackgroundColor = resources.getColorStateList(R.color.transparent)
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun setupRV() {
        adapter = FavoriteAdapter()

        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        adapter.setOnClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data: com.capstone.laperinapp.data.favorite.entity.Favorite) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, data.id)
                startActivity(intent)
            }
        })
    }

    private fun getData() {
        viewModel.getRecipesFavorite().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}