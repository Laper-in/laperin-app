package com.capstone.laperinapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.capstone.laperinapp.helper.Result
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.adapter.BookmarkProfileAdapter
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DataItemBookmark
import com.capstone.laperinapp.data.response.UserDetailResponse
import com.capstone.laperinapp.databinding.FragmentProfileBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.detail.DetailActivity
import com.capstone.laperinapp.ui.koleksi.KoleksiFragment
import com.capstone.laperinapp.ui.login.LoginActivity
import com.capstone.laperinapp.ui.profile.setting.SettingActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BookmarkProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        setupToolbar()
        setupRV()

        binding.btnLihatSemua.setOnClickListener { onClickFavorite() }
    }

    private fun onClickFavorite() {
        val botNav = activity?.findNavController(R.id.nav_host_fragment_activity_main2)
        botNav?.navigate(R.id.navigation_koleksi)
        onDestroyView()
    }

    private fun setupRV() {
        adapter = BookmarkProfileAdapter()
        binding.rvKoleksi.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvKoleksi.layoutManager = layoutManager

        viewModel.getAllBookmark("").observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.setOnClickCallback(object : BookmarkProfileAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItemBookmark) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, data.recipe.id)
                startActivity(intent)
            }
        })
    }

    private fun setupToolbar() {
        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit_profile -> {
                    onClickSetting()
                    true
                }

                R.id.menu_logout -> {
                    onClickLogout()
                    true
                }

                else -> false
            }
        }
    }

    private fun onClickSetting() {
        val intent = Intent(requireContext(), SettingActivity::class.java)
        startActivity(intent)
    }

    private fun onClickLogout() {
        AlertDialog.Builder(requireContext())
        .setTitle("Konfirmasi Keluar")
        .setMessage("Anda yakin ingin keluar?")
        .setPositiveButton("Ya") { _, _ ->
            viewModel.logout()

            viewModel.logoutUser()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
        .setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
    }

    private fun getData() {
        val pref = UserPreference.getInstance(requireActivity().dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val id = JWTUtils.getId(token)
        Log.i(TAG, "id: $id")
        setupDataUser()
    }

    private fun setupDataUser() {
        viewModel.getUser().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    showLoading(false)
                    dataUser(result.data)
                    Log.i(TAG, "setupDataUser: ${result.data}")
                }

                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "setupDataUser: ${result.error}")
                }

                is Result.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun dataUser(data: UserDetailResponse) {
        binding.tvUsernameProfil.text = data.data.username
        Glide.with(requireContext())
            .load(data.data.image)
            .circleCrop()
            .into(binding.imgUser)
        if (data.data.isPro) {
            binding.badge.visibility = View.VISIBLE
        } else {
            binding.badge.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        val binding = _binding
        if (binding != null) {
            if (isLoading) {
                binding.progressBarProfile.visibility = View.VISIBLE
            } else {
                binding.progressBarProfile.visibility = View.GONE
            }
        }
    }

    private fun showEmpty(isEmpty: Boolean) {
        val binding = _binding
        if (binding != null) {
            if (isEmpty) {
                binding.tvKoleksiKosong.visibility = View.VISIBLE
            } else {
                binding.tvKoleksiKosong.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ProfileFragment"
        const val EXTRA_EMAIL = "extra_email"
    }

}