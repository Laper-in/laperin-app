package com.capstone.laperinapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.LoadingStateAdapter
import com.capstone.laperinapp.adapter.MarginItemDecoration
import com.capstone.laperinapp.adapter.PopularRecipesAdapter
import com.capstone.laperinapp.adapter.RekomendasiRecipesAdapter
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DataDetailUser
import com.capstone.laperinapp.data.response.DataItemRecipes
import com.capstone.laperinapp.databinding.FragmentHomeBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.Result
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.detail.DetailActivity
import com.capstone.laperinapp.ui.home.search.SearchRecipesActivity
import com.capstone.laperinapp.ui.scan.result.ResultActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
    private var timer: Timer? = null

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

        getDataUser()
        setupRVRekomendasi()
        setupRVPopular()
        showDataPopular()
        showDataRekomendasi()
        setupSearch()

        binding.btnCariin.setOnClickListener { onClickCariin() }
        binding.btnDonasiin.setOnClickListener { onClickDonasiin() }
    }

    private fun onClickDonasiin() {
        val botNav = activity?.findNavController(R.id.nav_host_fragment_activity_main2)
        botNav?.navigate(R.id.navigation_donasi)
        onDestroyView()
    }

    private fun setupSearch() {
        binding.searchBar.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchRecipesActivity::class.java))
        }
    }

    private fun onClickCariin() {
        startActivity(Intent(requireActivity(), ResultActivity::class.java))
    }

    private fun getDataUser() {
        val pref = UserPreference.getInstance(requireActivity().dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val id = JWTUtils.getId(token)
        Log.i(TAG, "getDataUser: $token")

        viewModel.getUser().observe(viewLifecycleOwner) {result ->
            when(result){
                is Result.Success -> {
                    setupDataUser(result.data.data)
                }
                is Result.Error -> {
                    Log.e(TAG, "getDataUser: ${result.error}", )
                }
                is Result.Loading -> {
                    Log.d(TAG, "getDataUser: Loading")
                }
            }
        }
    }

    private fun setupDataUser(data: DataDetailUser) {
        binding.tvUsername.text = data.username
        Glide.with(requireActivity())
            .load(data.image)
            .circleCrop()
            .into(binding.imgProfile)
    }

    private fun setupRVRekomendasi() {
        val layoutManagerRekomendasi =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        val itemDecoration = MarginItemDecoration(50, 50)

        binding.apply {
            rvRekomendasi.layoutManager = layoutManagerRekomendasi
            rvRekomendasi.addItemDecoration(itemDecoration)
        }

        setupSlider(layoutManagerRekomendasi)
    }

    private fun setupRVPopular() {
        val layoutManagerPopular = LinearLayoutManager(requireActivity())
        binding.rvPopular.layoutManager = layoutManagerPopular

        binding.apply {
            rvPopular.layoutManager = layoutManagerPopular
            rvPopular.isNestedScrollingEnabled = false
        }
    }

    private fun setupSlider(layoutManagerRekomendasi: LinearLayoutManager) {
        val bindingRef = _binding

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvRekomendasi)

        timer = Timer()
        timer?.scheduleAtFixedRate(object : java.util.TimerTask() {
            override fun run() {
                if (bindingRef?.rvRekomendasi != null) {
                    val currentPosition =
                        layoutManagerRekomendasi.findLastCompletelyVisibleItemPosition()
                    Log.d(
                        TAG,
                        "posisi sekarang: $currentPosition , jumlah data: ${rekomendasiAdapter.itemCount}"
                    )

                    if (currentPosition < (rekomendasiAdapter.itemCount - 1)) {
                        layoutManagerRekomendasi.smoothScrollToPosition(
                            binding.rvRekomendasi,
                            RecyclerView.State(),
                            currentPosition + 1
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
        }, 0, 3000)
    }


    private fun showDataPopular() {
        binding.rvPopular.adapter = popularAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                popularAdapter.retry()
            }
        )

        viewModel.getAllRecipes().observe(viewLifecycleOwner) {
            popularAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        observeDataPopular()

        popularAdapter.setOnClickCallback(object : PopularRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemRecipes) {
                showSelectedItem(data)
            }
        })
    }

    private fun observeDataPopular() {
        popularAdapter.addLoadStateListener { loadState ->
            showLoading(loadState.refresh is LoadState.Loading)
        }
    }

    private fun showDataRekomendasi() {
        binding.rvRekomendasi.adapter = rekomendasiAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                rekomendasiAdapter.retry()
            }
        )

        viewModel.getAllRecipesRandom().observe(viewLifecycleOwner) {
            rekomendasiAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        rekomendasiAdapter.setOnClickCallback(object :
            RekomendasiRecipesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemRecipes) {
                showSelectedItem(data)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        val binding = _binding
        if (binding != null) {
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showSelectedItem(data: DataItemRecipes) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA, data.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timer?.cancel()
        timer = null
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