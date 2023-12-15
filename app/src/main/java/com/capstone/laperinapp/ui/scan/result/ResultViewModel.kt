package com.capstone.laperinapp.ui.scan.result

import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.room.result.entity.ScanResult

class ResultViewModel(val repository: Repository): ViewModel() {

    fun getAllResult() = repository.getAllResult()

    fun insertResult(result: ScanResult) = repository.insertResult(result)

    fun deleteAllResult() = repository.deleteAllResult()

    fun deleteById(result: ScanResult) = repository.deleteResultById(result)

}