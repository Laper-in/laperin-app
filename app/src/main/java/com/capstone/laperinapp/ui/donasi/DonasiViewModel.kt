package com.capstone.laperinapp.ui.donasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository

class DonasiViewModel(private val repository: Repository) : ViewModel() {

    fun getClosestDonation(longitude: Double, latitude: Double) = repository.getClosestDonation(longitude, latitude).cachedIn(viewModelScope)

    fun getAllDonation() = repository.getAllDonations().cachedIn(viewModelScope)
}