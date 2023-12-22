package com.capstone.laperinapp.ui.donasi.saya

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository

class DonasiSayaViewModel(private val repository: Repository): ViewModel() {

    fun myUncompletedDonations() = repository.getMyUncompletedDonations().cachedIn(viewModelScope)

    fun myCompletedDonations() = repository.getMyCompletedDonations().cachedIn(viewModelScope)

    fun selesaikanDonasi(id: String) = repository.deleteDonation(id)

    fun getUser() = repository.getDetailUser()
}