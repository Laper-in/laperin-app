package com.capstone.laperinapp.ui.scan.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.response.DataItemIngredient
import com.capstone.laperinapp.data.response.IngredientItem
import com.capstone.laperinapp.data.room.result.entity.ScanResult

class ResultViewModel(val repository: Repository): ViewModel() {

    val searchStringLiveData = MutableLiveData<String>()

    private val searchResults: LiveData<PagingData<DataItemIngredient>> = searchStringLiveData.switchMap { query ->
        if (query.isNullOrEmpty() || query.isBlank()) {
            repository.getIngredientsByName("").cachedIn(viewModelScope)
        }else {
            repository.getIngredientsByName(query).cachedIn(viewModelScope)
        }
    }

    fun insertIngredient(ingredient: ScanResult) = repository.insertResult(ingredient)

    fun getIngredientByName(): LiveData<PagingData<DataItemIngredient>> = searchResults

    fun getAllResult() = repository.getAllResult()

    fun insertResult(result: ScanResult) = repository.insertResult(result)

    fun deleteAllResult() = repository.deleteAllResult()

    fun deleteById(result: ScanResult) = repository.deleteResultById(result)

}