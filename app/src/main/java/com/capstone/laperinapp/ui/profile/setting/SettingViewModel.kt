package com.capstone.laperinapp.ui.profile.setting

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.ui.login.LoginActivity
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: Repository): ViewModel() {

    fun getDetailUser(id: String) = repository.getDetailUser(id)

    fun logout() = viewModelScope.launch { repository.logout() }
}