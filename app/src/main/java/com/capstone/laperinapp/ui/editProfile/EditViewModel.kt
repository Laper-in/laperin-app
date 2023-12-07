package com.capstone.laperinapp.ui.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserModel
import kotlinx.coroutines.launch

class EditViewModel(private  val repository: Repository) :ViewModel() {

    fun updateUser(id: String, password: String, email: String, fullname: String, picture: String, alamat: String, telephone: Int) =
        repository.updateDataUser(id, password, email, fullname, picture, alamat, telephone)

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}