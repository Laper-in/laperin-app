package com.capstone.laperinapp.ui.scan.search

import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.room.result.entity.ScanResult

class SearchViewModel(val repository: Repository): ViewModel() {

    fun getAllIngredients() = repository.getAllIngredients()

    fun insertIngredient(ingredient: ScanResult) = repository.insertResult(ingredient)
}