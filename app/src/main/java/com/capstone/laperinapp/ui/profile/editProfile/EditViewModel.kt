package com.capstone.laperinapp.ui.profile.editProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.data.pref.UserModel
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigInteger

class EditViewModel(private  val repository: Repository) :ViewModel() {

    fun updateUser(
        id: String,
        email: String,
        fullname: String,
        alamat: String,
        telephone: BigInteger
    ) = repository.updateUser(id, email, fullname, alamat, telephone)

}