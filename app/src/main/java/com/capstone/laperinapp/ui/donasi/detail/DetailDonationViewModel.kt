package com.capstone.laperinapp.ui.donasi.detail

import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository

class DetailDonationViewModel(private val repository: Repository): ViewModel() {

    fun getDetailDonation(idDonation: String) = repository.getDetailDonation(idDonation)

    fun selesaikanDonasi(id: String) = repository.deleteDonation(id)
}