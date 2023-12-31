package com.capstone.laperinapp.ui.donasi.add

import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddDonationViewModel(private val repository: Repository): ViewModel() {

    fun sendDonation(
        username: RequestBody,
        name: RequestBody,
        description: RequestBody,
        category: RequestBody,
        total: RequestBody,
        longitude: RequestBody,
        latitude: RequestBody,
        image: MultipartBody.Part,
        userImage: RequestBody,
        telephone: RequestBody
    ) = repository.createDonation(username, name, description, category, total, longitude, latitude, image, userImage, telephone)

    fun getUser() = repository.getDetailUser()

    companion object {
        private const val TAG = "AddDonationViewModel"
    }
}