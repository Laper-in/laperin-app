package com.capstone.laperinapp.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserModel
import kotlinx.coroutines.launch

class EditViewModel (private var repository: Repository) :ViewModel(){

    fun editDataUser(id: String, name: String, email: String, password: String) =
        repository.editProfile(id, name, email, password)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}