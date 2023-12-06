package com.capstone.laperinapp.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.capstone.laperinapp.helper.Result
import androidx.fragment.app.viewModels
import com.capstone.laperinapp.data.pref.UserPreference
import com.capstone.laperinapp.data.pref.dataStore
import com.capstone.laperinapp.data.response.DetailUserResponse
import com.capstone.laperinapp.databinding.FragmentProfileBinding
import com.capstone.laperinapp.helper.JWTUtils
import com.capstone.laperinapp.helper.ViewModelFactory
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
    private fun dataUser(data : DetailUserResponse) {
        binding.tvUsernameProfil.text = data.username
        binding.tvEmailUser.text = data.email
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
    }

}