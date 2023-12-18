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
import com.capstone.laperinapp.data.response.DataItemDonation
import com.capstone.laperinapp.data.response.IngredientItem

class DonasiViewModel(private val repository: Repository) : ViewModel() {

    val lonLatLiveData = MutableLiveData<Pair<Float, Float>>()

    private val donationResults: LiveData<PagingData<DataItemDonation>> = lonLatLiveData.switchMap { (lon, lat) ->
        if (lon == 0.toFloat()) {
            repository.getClosestDonation(0F, 0F).cachedIn(viewModelScope)
        } else {
            repository.getClosestDonation(lon, lat).cachedIn(viewModelScope)
        }
    }

    fun getClosestDonation() = donationResults

}