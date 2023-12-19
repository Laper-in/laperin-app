package com.capstone.laperinapp.ui.scan.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.room.result.entity.ScanResult
import kotlinx.coroutines.launch

class PreviewViewModel(val repository: Repository): ViewModel() {

    fun insertResult(result: ScanResult) = repository.insertResult(result)

    fun getAllResult() = repository.getAllResult()

    fun containsIngredient(ingredient: String): LiveData<Boolean> {
        val resultLiveData = MutableLiveData<Boolean>()

        viewModelScope.launch {
            val count = repository.getAllResultContainsName(ingredient)
            resultLiveData.postValue(count > 0)
        }

        return resultLiveData
    }

}