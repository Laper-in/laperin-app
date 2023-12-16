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
import com.bumptech.glide.Glide
import com.capstone.laperinapp.R
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DataUser
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.databinding.FragmentProfileBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
import com.capstone.laperinapp.ui.profile.editProfile.EditProfilActivity
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
    }

    private fun setupToolbar() {
        binding.appBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
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
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Konfirmasi Keluar")
        alertDialogBuilder.setMessage("Anda yakin ingin keluar?")
        alertDialogBuilder.setPositiveButton("Ya") { _, _ ->
            viewModel.logout()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
        alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }
    
    private fun getData() {
        val pref = UserPreference.getInstance(requireActivity().dataStore)
        val user = runBlocking { pref.getSession().first() }
        val token = user.token
        val id = JWTUtils.getId(token)
        Log.i(TAG, "id: $id")
        setupDataUser(id)
    }

    private  fun setupDataUser(id :String?)  {
        if (id != null) {
            viewModel.getUser(id).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Success -> {
                        showLoading(false)
                        dataUser(result.data)
                        Log.i(TAG, "setupDataUser: ${result.data}")
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "setupDataUser: ${result.error}", )
                    }

                    is Result.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
    }
    private fun dataUser(data : DataUser) {
        binding.tvUsernameProfil.text = data.fullname
        if (data.alamat == null) {
            binding.tvAlamat.text = getString(R.string.alamat_kosong)
        } else {
            binding.tvAlamat.text = data.alamat
        }
        Glide.with(requireContext())
            .load(data.picture)
            .circleCrop()
            .into(binding.imgUser)
        if (data.isPro){
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        private const val TAG = "ProfileFragment"
        const val EXTRA_EMAIL = "extra_email"
    }

}