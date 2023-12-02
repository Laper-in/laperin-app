package com.capstone.laperinapp.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.CategoryAdapter
import com.capstone.laperinapp.adapter.MarginItemDecoration
import com.capstone.laperinapp.adapter.PopularRecipesAdapter
import com.capstone.laperinapp.adapter.RekomendasiRecipesAdapter
import com.capstone.laperinapp.data.model.Category
import com.capstone.laperinapp.data.response.DataRecipes
import com.capstone.laperinapp.databinding.FragmentHomeBinding
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import java.lang.ref.WeakReference
import java.util.Timer
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? by weak()
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val popularAdapter = PopularRecipesAdapter()
    private val rekomendasiAdapter = RekomendasiRecipesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupData()
    }

    private fun setupData() {
        viewModel.getAllRecipes().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(false)
                    showDataPopular(result.data)
                    showDataRekomendasi(result.data)
                }

                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "errorGetData: ${result.error}")
                }

                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val layoutManagerPopular = GridLayoutManager(requireActivity(), 2)
        binding.rvPopular.layoutManager = layoutManagerPopular

        val layoutManagerRekomendasi =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val itemDecoration = MarginItemDecoration(50, 50)

        setupSlider(layoutManagerRekomendasi)
        setupRVCategory()

        binding.apply {
            rvPopular.layoutManager = layoutManagerPopular
            rvPopular.setHasFixedSize(true)
            rvRekomendasi.layoutManager = layoutManagerRekomendasi
            rvRekomendasi.addItemDecoration(itemDecoration)
        }
    }

    private fun setupRVCategory() {
        val listCategory = ArrayList<Category>()
        listCategory.addAll(showDataCategory())
        val categoryAdapter = CategoryAdapter(listCategory)

        val layoutManagerCategory = GridLayoutManager(requireActivity(), 4)

        binding.apply {
            rvCategory.layoutManager = layoutManagerCategory
            rvCategory.setHasFixedSize(true)
            rvCategory.adapter = categoryAdapter
        }


        categoryAdapter.setOnClickCallback(object : CategoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Category) {
                Toast.makeText(requireContext(), data.name, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSlider(layoutManagerRekomendasi: LinearLayoutManager) {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvRekomendasi)

        val timer = Timer()
        timer.schedule(object : java.util.TimerTask() {
            override fun run() {
                val bindingRef = _binding
                if (bindingRef?.rvRekomendasi != null){
                    if (layoutManagerRekomendasi.findLastCompletelyVisibleItemPosition() < (rekomendasiAdapter.itemCount - 1)) {
                        layoutManagerRekomendasi.smoothScrollToPosition(
                            binding.rvRekomendasi,
                            RecyclerView.State(),
                            layoutManagerRekomendasi.findLastCompletelyVisibleItemPosition() + 1
                        )
                    } else {
                        layoutManagerRekomendasi.smoothScrollToPosition(
                            binding.rvRekomendasi,
                            RecyclerView.State(),
                            0
                        )
                    }
                }
            }
        }, 2000, 6000)
    }

    private fun showDataPopular(data: List<DataRecipes>) {
        popularAdapter.submitList(data)
        binding.rvPopular.adapter = popularAdapter

        popularAdapter.setOnClickCallback(object : PopularRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataRecipes) {
                showSelectedItem(data)
            }
        })
    }

    private fun showDataRekomendasi(data: List<DataRecipes>) {
        rekomendasiAdapter.submitList(data)
        binding.rvRekomendasi.adapter = rekomendasiAdapter

        rekomendasiAdapter.setOnClickCallback(object :
            RekomendasiRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataRecipes) {
                showSelectedItem(data)
            }
        })
    }

    @SuppressLint("Recycle")
    private fun showDataCategory(): ArrayList<Category> {
        val catName = resources.getStringArray(R.array.category_name)
        val catImage = resources.obtainTypedArray(R.array.category_icon)
        val listCategory = ArrayList<Category>()
        for (position in catName.indices) {
            val category = Category(
                catName[position],
                catImage.getResourceId(position, -1)
            )
            listCategory.add(category)
        }
        return listCategory
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showSelectedItem(data: DataRecipes) {
        Toast.makeText(requireContext(), data.name, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}

fun <T : Any> weak(): ReadWriteProperty<Any?, T?> = WeakReferenceProperty()
class WeakReferenceProperty<T : Any> : ReadWriteProperty<Any?, T?> {
    private var weakReference: WeakReference<T?> = WeakReference(null)

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return weakReference.get()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weakReference = WeakReference(value)
    }
}