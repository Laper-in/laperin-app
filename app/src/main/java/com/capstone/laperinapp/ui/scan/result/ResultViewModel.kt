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
import com.capstone.laperinapp.data.response.ResponseSearchIngredient
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import com.capstone.laperinapp.helper.Result
import kotlinx.coroutines.launch

class ResultViewModel(val repository: Repository): ViewModel() {

    val searchStringLiveData = MutableLiveData<String>()
    val searchResultLiveData = MutableLiveData<String>().apply { value = "" }

    private val searchIngredient: LiveData<PagingData<DataItemIngredient>> = searchStringLiveData.switchMap { query ->
        if (query.isNullOrEmpty() || query.isBlank()) {
            repository.getIngredientsByName("").cachedIn(viewModelScope)
        }else {
            repository.getIngredientsByName(query).cachedIn(viewModelScope)
        }
    }

    private val searchResult: LiveData<Result<ResponseSearchIngredient>> = searchResultLiveData.switchMap { query ->
        if (query.isNullOrEmpty()) {
            repository.searchResultScan("")
        } else {
            repository.searchResultScan(query)
        }
    }

    fun containsIngredient(ingredient: String): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()

        viewModelScope.launch {
            val count = repository.getAllResultContainsName(ingredient)
            resultLiveData.postValue(count > 0)
        }

        return resultLiveData
    }

    fun insertIngredient(ingredient: ScanResult) = repository.insertResult(ingredient)

    fun getIngredientByName(): LiveData<PagingData<DataItemIngredient>> = searchIngredient

    fun searchResult(): LiveData<Result<ResponseSearchIngredient>> = searchResult

    fun getAllResult() = repository.getAllResult()

    fun getAllResultName() = repository.getAllResultName()

    fun deleteAllResult() = repository.deleteAllResult()

    fun deleteById(result: ScanResult) = repository.deleteResultById(result)

}