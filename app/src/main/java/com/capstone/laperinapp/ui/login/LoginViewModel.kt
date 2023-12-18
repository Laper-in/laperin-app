package com.capstone.laperinapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserModel
import com.capstone.laperinapp.data.response.LoginResponse
import kotlinx.coroutines.launch
import com.capstone.laperinapp.helper.Result

class LoginViewModel(
    private val repository: Repository
) : ViewModel() {

    fun setLogin(username : String, password : String) = repository.setLogin(username, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}