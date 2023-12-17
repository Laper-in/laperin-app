package com.capstone.laperinapp.ui.donasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.response.ClosestDonationsItem
import com.capstone.laperinapp.data.response.IngredientItem

class DonasiViewModel(private val repository: Repository) : ViewModel() {

    fun getAllDonation() = repository.getAllDonations().cachedIn(viewModelScope)

    val lonLatLiveData = MutableLiveData<Pair<Double, Double>>()

    private val donationResults: LiveData<PagingData<ClosestDonationsItem>> = lonLatLiveData.switchMap { (lon, lat) ->
        if (lon == 0.0) {
            repository.getClosestDonation(0.0, 0.0).cachedIn(viewModelScope)
        } else {
            repository.getClosestDonation(lon, lat).cachedIn(viewModelScope)
        }
    }

    fun getClosestDonation() = donationResults

}