package com.capstone.laperinapp.ui.donasi.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddDonationViewModel(private val repository: Repository): ViewModel() {

    suspend fun sendDonation(
        userId: RequestBody,
        username: RequestBody,
        name: RequestBody,
        description: RequestBody,
        category: RequestBody,
        total: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
        image: MultipartBody.Part
    ) = repository.createDonation(userId, username, name, description, category, total, longitude, latitude, image)

    fun sendsDonation(
        userId: RequestBody,
        username: RequestBody,
        name: RequestBody,
        description: RequestBody,
        category: RequestBody,
        total: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
        image: MultipartBody.Part
    ) = repository.addsDonations(userId, username, name, description, category, total, longitude, latitude, image)

    companion object {
        private const val TAG = "AddDonationViewModel"
    }
}