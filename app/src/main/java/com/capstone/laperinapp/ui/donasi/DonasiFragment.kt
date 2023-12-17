package com.capstone.laperinapp.ui.donasi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.laperinapp.adapter.ClosestDonationAdapter
import com.capstone.laperinapp.adapter.DonationAdapter
import com.capstone.laperinapp.adapter.LoadingStateAdapter
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.data.response.DonationsItem
import com.capstone.laperinapp.databinding.FragmentDonasiBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.donasi.add.AddDonasiActivity
import com.capstone.laperinapp.ui.donasi.detail.DetailDonationActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DonasiFragment : Fragment() {

    private var _binding: FragmentDonasiBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<DonasiViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLatitude: Double = 0.0
    private var userLongitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDonasiBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        
        getCurrentLocation()
        setupDataAll()
        binding.btnAddDonation.setOnClickListener { moveToAddDonation() }
    }

    private fun moveToAddDonation() {
        val pref = UserPreference.getInstance(requireActivity().dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val id = JWTUtils.getId(token)
        val username = JWTUtils.getUsername(token)
        val intent = Intent(requireActivity(), AddDonasiActivity::class.java)
        intent.putExtra(AddDonasiActivity.EXTRA_ID, id)
        intent.putExtra(AddDonasiActivity.EXTRA_USERNAME, username)
        intent.putExtra(AddDonasiActivity.EXTRA_LATITUDE, userLatitude)
        intent.putExtra(AddDonasiActivity.EXTRA_LONGITUDE, userLongitude)
        startActivity(intent)
    }

    private fun setupDataAll() {
        val adapter = DonationAdapter()
        binding.rvLatest.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvLatest.layoutManager = layoutManager

        viewModel.getAllDonation().observe(viewLifecycleOwner){ result ->
            adapter.submitData(viewLifecycleOwner.lifecycle, result)
        }
        adapter.setOnClickCallback(object : DonationAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DonationsItem) {
                navigateToDetailPage(data)
            }
        })
    }

    private fun navigateToDetailPage(data: DonationsItem) {
        val intent = Intent(requireActivity(), DetailDonationActivity::class.java)
        intent.putExtra(DetailDonationActivity.EXTRA_DATA, data)
        startActivity(intent)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                    getCurrentLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    getCurrentLocation()
                }
                else -> {
                    // Permission denied
                }
            }
        }
    
    private fun checkPermission(permissions: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permissions
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun getCurrentLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    setupDataClosest(location.longitude, location.latitude)
                    userLatitude = location.latitude
                    userLongitude = location.longitude
                } else {
                    Toast.makeText(requireContext(), "Lokasi tidak ditemukan, Coba lagi", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun setupDataClosest(longitude: Double, latitude: Double) {
        val adapter = ClosestDonationAdapter()
        binding.rvClosest.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvClosest.layoutManager = layoutManager

        viewModel.getClosestDonation(longitude, latitude).observe(viewLifecycleOwner){ result ->
            adapter.submitData(viewLifecycleOwner.lifecycle, result)
        }

        adapter.setOnClickCallback(object : ClosestDonationAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ClosestDonationsItem) {
                val intent = Intent(requireActivity(), DetailDonationActivity::class.java)
                intent.putExtra(DetailDonationActivity.EXTRA_DATA, data)
                startActivity(intent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        private const val TAG = "DonasiFragment"
    }
}