package com.capstone.laperinapp.ui.scan.recommendation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagingData
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.response.DataItemRecipes

class RecommendationViewModel(private val repository: Repository): ViewModel() {

    val searchLiveData = MutableLiveData<String>().apply { value = "" }

    private val searchResult: LiveData<PagingData<DataItemRecipes>> = searchLiveData.switchMap { query ->
        if (query == "") {
            repository.getRecommendation("")
        }else {
            repository.getRecommendation(query)
        }
    }

    fun searchRecommendation() = searchResult

    fun getAllResult() = repository.getAllResultName()
}