package com.capstone.laperinapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.response.DataDetailRecipes

class SharedViewModel : ViewModel() {
    private val selectedDetail = MutableLiveData<DataDetailRecipes>()

    fun setSelectedDetail(detail: DataDetailRecipes) {
        selectedDetail.value = detail
    }

    fun getSelectedDetail(): LiveData<DataDetailRecipes> {
        return selectedDetail
    }
}
