package com.capstone.laperinapp.ui.splashScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.SettingPreferences
import com.capstone.laperinapp.data.pref.UserModel

class SplashViewModel(private val repository: Repository, private val pref: SettingPreferences): ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}