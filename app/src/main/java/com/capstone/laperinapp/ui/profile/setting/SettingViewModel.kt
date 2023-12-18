package com.capstone.laperinapp.ui.profile.setting

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.SettingPreferences
import com.capstone.laperinapp.ui.login.LoginActivity
import kotlinx.coroutines.launch

class SettingViewModel(private val repository: Repository, private val pref: SettingPreferences): ViewModel() {

    fun getDetailUser() = repository.getDetailUser()

    fun logout() = viewModelScope.launch { repository.logout() }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkMode)
        }
    }
}