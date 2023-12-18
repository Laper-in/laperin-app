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
        email: String,
        fullname: String,
        alamat: String,
        telephone: BigInteger
    ) = repository.updateUser(email, fullname, alamat, telephone)

}