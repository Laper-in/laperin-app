package com.capstone.laperinapp.ui.koleksi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.paging.LoadState
import androidx.paging.log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.BookmarkAdapter
import com.capstone.laperinapp.adapter.StaggeredItemDecoration
import com.capstone.laperinapp.data.response.DataItemBookmark
import com.capstone.laperinapp.databinding.FragmentKoleksiBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
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
    private lateinit var adapter: BookmarkAdapter

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

    private fun setupCategory() {
        viewModel.getCategory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    configureCategory(result.data)
                }

                is Result.Error -> {
                    showEmpty(isEmpty = true)
                }

                else -> false
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun configureCategory(data: String) {
        val chipGroup: ChipGroup = binding.chipCategory
        val chipCategory = listOf("All") + data.split(", ")

        for (category in chipCategory) {
            val chip = Chip(requireContext())
            chip.shapeAppearanceModel = chip.shapeAppearanceModel
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, 50f)
                .build()
            chip.text = category
            chip.isCheckable = true
            if (category == "All") {
                chip.isChecked = true
                chip.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.orange))
                chip.chipBackgroundColor = resources.getColorStateList(R.color.light_orange)
                viewModel.searchStringLiveData.value = category
            }

            chip.setOnCheckedChangeListener() { buttonView, isChecked ->
                if (isChecked) {
                    buttonView.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.orange
                        )
                    )
                    chip.chipBackgroundColor = resources.getColorStateList(R.color.light_orange)
                    viewModel.searchStringLiveData.value = category
                } else {
                    buttonView.setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.black
                        )
                    )
                    chip.chipBackgroundColor = resources.getColorStateList(R.color.transparent)
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun setupRV() {
        adapter = BookmarkAdapter()

        binding.rvFavorite.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.addItemDecoration(StaggeredItemDecoration(50))

        adapter.setOnClickCallback(object : BookmarkAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemBookmark, holder: BookmarkAdapter.ViewHolder) {
                holder.itemView.setOnClickListener {
                    val intent = Intent(requireContext(), DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DATA, data.recipe.id)
                    startActivity(intent)
                }
            }
        })

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.NotLoading) {
                if (adapter.itemCount == 0) {
                    showEmpty(isEmpty = true)
                } else {
                    showEmpty(isEmpty = false)
                }
            }
        }
    }

    private fun getData() {
        viewModel.getAllBookmarks().observe(viewLifecycleOwner) { result ->
            adapter.submitData(viewLifecycleOwner.lifecycle, result)
        }
    }

    private fun showEmpty(isEmpty: Boolean) {
        if (isEmpty) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.rvFavorite.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.rvFavorite.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "KoleksiFragment"
    }
}