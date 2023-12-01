package com.capstone.laperinapp.ui.splashScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserModel

class SplashViewModel(val repository: Repository): ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}