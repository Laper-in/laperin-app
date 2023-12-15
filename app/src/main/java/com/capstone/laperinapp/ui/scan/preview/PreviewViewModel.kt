package com.capstone.laperinapp.ui.scan.preview

import androidx.lifecycle.ViewModel
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.room.result.entity.ScanResult

class PreviewViewModel(val repository: Repository): ViewModel() {

    fun insertResult(result: ScanResult) = repository.insertResult(result)

}