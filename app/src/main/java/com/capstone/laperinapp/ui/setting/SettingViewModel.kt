package com.capstone.laperinapp.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: Repository) :ViewModel() {

    fun getUser(id:String) = repository.getDetailUser(id)

     fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}