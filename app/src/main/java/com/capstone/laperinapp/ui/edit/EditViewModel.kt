package com.capstone.laperinapp.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserModel
import kotlinx.coroutines.launch

class EditViewModel (private var repository: Repository) :ViewModel(){

    fun editDataUser(id :String, fullname: String,picture :String, alamat :String, telephone :Int ) =
        repository.editProfile(id, fullname, picture, alamat, telephone)


//    fun saveSession(user: UserModel) {
//        viewModelScope.launch {
//            repository.saveSession(user)
//        }
//    }
}