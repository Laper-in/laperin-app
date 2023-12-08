package com.capstone.laperinapp.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.laperinapp.data.Repository
import com.capstone.laperinapp.di.Injection
import com.capstone.laperinapp.ui.detail.DetailViewModel
import com.capstone.laperinapp.ui.edit.EditViewModel
import com.capstone.laperinapp.ui.donasi.DonasiViewModel
import com.capstone.laperinapp.ui.home.HomeViewModel
import com.capstone.laperinapp.ui.login.LoginViewModel
import com.capstone.laperinapp.ui.profile.ProfileViewModel
import com.capstone.laperinapp.ui.register.RegisterViewModel
import com.capstone.laperinapp.ui.setting.SettingViewModel
import com.capstone.laperinapp.ui.splashScreen.SplashViewModel
import com.capstone.laperinapp.ui.welcome.WelcomeViewModel

class ViewModelFactory private constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->{
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditViewModel::class.java) -> {
                EditViewModel(repository)  as T
            modelClass.isAssignableFrom(DonasiViewModel::class.java) -> {
                DonasiViewModel(repository) as T
            }
             else ->    throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }

        fun clearInstance(){
            Repository.clearInstance()
            instance = null
        }
    }
}